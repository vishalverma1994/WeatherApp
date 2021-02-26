package com.weather.ui.base

import android.content.pm.ActivityInfo
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.weather.R
import com.weather.listeners.FragmentSelectionListener
import com.weather.ui.fragments.WeatherFragment
import com.weather.utils.FRAG_WEATHER_LIST

abstract class BaseActivity : AppCompatActivity(), FragmentSelectionListener {

    private val TAG = BaseActivity::class.simpleName
    lateinit var fragmentManager: FragmentManager
    lateinit var fragmentTransaction: FragmentTransaction
    var fragReqCode = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
        fragmentManager = supportFragmentManager
    }

    override fun onBackPressed() {
        navigateBack()
    }

    override fun finish() {
        super.finish()
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right)
    }

    fun navigateBack() {
        if (::fragmentManager.isInitialized && fragmentManager.backStackEntryCount > 1) fragmentManager.popBackStack() else finish()
    }

    //Fragments
    fun getFragment(reqCode: Int, data: Bundle?): Fragment? {
        when (reqCode) {
            FRAG_WEATHER_LIST -> return WeatherFragment.newInstance(data)
        }
        return null
    }
}