package com.weather.listeners

import android.os.Bundle

interface FragmentSelectionListener {
    fun onFragmentSelected(reqCode: Int, data: Bundle?)
}