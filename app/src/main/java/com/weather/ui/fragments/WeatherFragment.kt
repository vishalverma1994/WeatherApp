package com.weather.ui.fragments

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.LocationManager
import android.os.Bundle
import android.provider.Settings
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.LinearLayoutManager
import com.weather.R
import com.weather.data.model.WeatherData
import com.weather.databinding.FragmentWeatherBinding
import com.weather.listeners.RetryListener
import com.weather.ui.activity.MainActivity
import com.weather.ui.adapter.WeatherAdapter
import com.weather.ui.base.BaseFragment
import com.weather.ui.viewmodel.MainViewModel
import com.weather.utils.*
import org.koin.androidx.viewmodel.ext.android.viewModel

class WeatherFragment : BaseFragment(), RetryListener {

    private lateinit var binding: FragmentWeatherBinding
    private val mainViewModel: MainViewModel by viewModel()

    private lateinit var weatherAdapter: WeatherAdapter
    private var weatherList = emptyList<WeatherData>()

    private lateinit var gpsTracker: GpsTracker

    companion object {
        private val TAG = WeatherFragment::class.simpleName
        fun newInstance(args: Bundle?) =
            WeatherFragment().apply {
                arguments = args
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentWeatherBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission ok. Do work.
                getLocation()
            }
        }
    }

    override fun onRetryButtonCallBack() {

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initComponents()
        requestUserPostsAPI()
        observeUserPostsListResponse()
    }

    private fun initComponents() {
        binding.handler = this
        (requireActivity() as MainActivity).supportActionBar?.title =
            getString(R.string.weather_app)
        checkOrAskLocationPermission {
            getLocation()
        }
        setUserPostsAdapter()
    }

    //location fetch code starts
    private fun checkOrAskLocationPermission(callback: () -> Unit) {
        // Check GPS is enabled
        val lm = requireActivity().getSystemService(Context.LOCATION_SERVICE) as LocationManager
        if (!lm.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Utils.showToast(requireContext(), "Please enable location services")
            buildAlertMessageNoGps(requireContext())
            return
        }

        // Check location permission is granted - if it is, start
        // the service, otherwise request the permission
        val permission = ContextCompat.checkSelfPermission(
            requireContext(),
            Manifest.permission.ACCESS_FINE_LOCATION
        )
        if (permission == PackageManager.PERMISSION_GRANTED) {
            callback.invoke()
        } else {
            // callback will be inside the activity's onRequestPermissionsResult(
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST
            )
        }
    }

    private fun buildAlertMessageNoGps(context: Context) {
        val builder = AlertDialog.Builder(context);
        builder.setMessage("Your GPS is disabled. Do you want to enable it?")
            .setCancelable(false)
            .setPositiveButton("Yes") { _, _ -> context.startActivity(Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS)) }
            .setNegativeButton("No") { dialog, _ -> dialog.cancel(); }
        val alert = builder.create();
        alert.show();
    }

    private fun setUserPostsAdapter() {
        binding.rvWeather.layoutManager = LinearLayoutManager(requireContext())
        weatherAdapter = WeatherAdapter(::onItemClickListener)
        binding.rvWeather.adapter = weatherAdapter
    }

    private fun getLocation() {
        gpsTracker = GpsTracker(requireContext())
        if (gpsTracker.canGetLocation()) {
            val latitude: Double = gpsTracker.getLatitude()
            val longitude: Double = gpsTracker.getLongitude()
            LogUtils.e(TAG, "Location : $latitude - $longitude")
            binding.latitude = latitude
            binding.longitude = longitude
            if (::gpsTracker.isInitialized)
                gpsTracker.stopUsingGPS()
        } else {
            gpsTracker.showSettingsAlert()
        }
    }

    private fun onItemClickListener(position: Int) {

    }

    //api request to fetch user posts
    private fun requestUserPostsAPI() {
        mainViewModel.requestWeatherAPI()
    }

    //method is used to observe the user posts list
    private fun observeUserPostsListResponse() {
        mainViewModel._weatherListResponse.observe(viewLifecycleOwner, {
            when (it.status) {
                Status.SUCCESS -> {
                    binding.isProgressBarVisible = false
                    binding.isDataFetched = true
                    binding.isEmptyStateVisible = false
                    it.data?.let { weatherList ->
                        this.weatherList = weatherList
                        weatherAdapter.setWeatherList(weatherList)
                    }
                }
                Status.LOADING -> {
                    binding.isProgressBarVisible = true
                    binding.isDataFetched = false
                    binding.isEmptyStateVisible = false
                }
                Status.ERROR -> {
                    binding.isProgressBarVisible = false
                    binding.isDataFetched = false
                    binding.isEmptyStateVisible = true
                    Utils.showToast(requireContext(), it.message)
                }
            }
        })
    }
}

