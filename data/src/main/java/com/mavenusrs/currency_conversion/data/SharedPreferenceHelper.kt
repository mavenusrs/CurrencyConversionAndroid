package com.mavenusrs.currency_conversion.data

import android.content.Context
import android.content.SharedPreferences
import android.os.SystemClock
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class SharedPreferenceHelper @Inject constructor(@ApplicationContext private val context: Context) {

    private fun getSharedPreference(): SharedPreferences {
        return context.getSharedPreferences(sharedPreferencesName, Context.MODE_PRIVATE)
    }

    fun getAPITimeStamp(path: String): Long {
        return getSharedPreference().getLong(path, 0)
    }

    fun putAPITimeStamp(path: String, timeStamp: Long = SystemClock.elapsedRealtime()) {
        getSharedPreference().edit()
            .putLong(path, timeStamp)
            .apply()
    }

    companion object {
        const val sharedPreferencesName = "apiPref"
    }
}