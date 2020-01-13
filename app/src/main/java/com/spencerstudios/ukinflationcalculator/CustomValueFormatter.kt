package com.spencerstudios.ukinflationcalculator

import com.github.mikephil.charting.formatter.ValueFormatter
import java.util.*

class CustomValueFormatter : ValueFormatter() {
    override fun getFormattedValue(value: Float): String {
        return if (value == 0f) ""
        else String.format(Locale.getDefault(), "£%.2f", value)
    }
}