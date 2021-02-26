package com.weather.utils

import android.app.Activity
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import java.text.SimpleDateFormat
import java.util.*

object Utils {

    fun hideSoftKeyBoard(mContext: Context) {
        try {
            val inputMethodManager =
                mContext.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            val view = (mContext as Activity).findViewById<View>(android.R.id.content)
            inputMethodManager.hideSoftInputFromWindow(
                view.windowToken,
                InputMethodManager.HIDE_NOT_ALWAYS
            )
        } catch (e: Throwable) {
            e.stackTrace.toString()
        }
    }

    fun showToast(context: Context, message: String?) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }
    }

    @JvmStatic
    fun epochToIso(time: Long): String? {
        val format = "MMMM d u"
        val sdf = SimpleDateFormat(format, Locale.getDefault())
        sdf.timeZone = TimeZone.getDefault()
        return sdf.format(Date(time * 1000))
    }
}