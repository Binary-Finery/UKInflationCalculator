package com.spencerstudios.ukinflationcalculator.utilities

import android.content.Context
import android.preference.PreferenceManager

class PrefUtils(context: Context) {

    private val prefs = PreferenceManager.getDefaultSharedPreferences(context)

    fun setYear1(y1: Int) {
        prefs.edit().putInt("y1", y1).apply()
    }

    fun getYear1(): Int {
        return prefs.getInt("y1", 234)
    }

    fun setYear2(y2: Int) {
        prefs.edit().putInt("y2", y2).apply()
    }

    fun getYear2(): Int {
        return prefs.getInt("y2", 268)
    }
}