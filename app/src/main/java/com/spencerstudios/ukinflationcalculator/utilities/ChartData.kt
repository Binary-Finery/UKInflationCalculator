package com.spencerstudios.ukinflationcalculator.utilities

import android.content.Context
import com.github.mikephil.charting.data.BarEntry
import java.util.*
import kotlin.math.max
import kotlin.math.min

class ChartData(ctx: Context) {

    private val prefs = PrefUtils(ctx)
    private val yearOne = prefs.getYear1()
    private val yearTwo = prefs.getYear2()

    fun getDates(): ArrayList<String> {
        val dates = ArrayList<String>()
        var y1 = yearOne
        var y2 = yearTwo

        when {
            y1 > y2 -> {
                val temp = y1
                y1 = y2
                y2 = temp
            }
        }
        (y1..y2).mapTo(dates) {
            MetaBuilder().buildYearMeta()[it].year.toString()
        }
        return dates
    }

    fun getData(amt: Double): ArrayList<BarEntry> {
        val data = ArrayList<BarEntry>()
        val ymin = min(yearOne, yearTwo)
        val ymax = max(yearOne, yearTwo)

        val meta = MetaBuilder().buildYearMeta()

        for ((idx, i) in (ymin..ymax).withIndex()) {
            data.add(BarEntry(idx.toFloat(), ((meta[i].value / meta[yearOne].value) * amt).toFloat()))
        }
        return data
    }
}