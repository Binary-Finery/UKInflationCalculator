package com.spencerstudios.ukinflationcalculator.formatters

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.ValueFormatter

class DatesFormatter(private val dates : ArrayList<String>) : ValueFormatter() {
    override fun getAxisLabel(value: Float, axis: AxisBase?): String {
        try {
            return dates[value.toInt()]
        }catch (e : Exception){
            e.printStackTrace()
        }
        return ""
    }
}