package com.spencerstudios.ukinflationcalculator.activities

import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.Toast
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.spencerstudios.ukinflationcalculator.R
import com.spencerstudios.ukinflationcalculator.dialogs.displayAboutDialog
import com.spencerstudios.ukinflationcalculator.formatters.CustomValueFormatter
import com.spencerstudios.ukinflationcalculator.formatters.DatesFormatter
import com.spencerstudios.ukinflationcalculator.utilities.ChartData
import com.spencerstudios.ukinflationcalculator.utilities.MetaBuilder
import com.spencerstudios.ukinflationcalculator.utilities.PrefUtils
import com.spencerstudios.ukinflationcalculator.utilities.share
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var barChart: BarChart
    private lateinit var yearArray: Array<String?>
    private lateinit var prefs: PrefUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        barChart = findViewById(R.id.chart)
        prefs = PrefUtils(this)
        yearArray = MetaBuilder().getYearArray()

        tvDateOne.text = yearArray[prefs.getYear1()]
        tvDateTwo.text = yearArray[prefs.getYear2()]
        tvDateOne.setOnClickListener { yearPickerDialog(1) }
        tvDateTwo.setOnClickListener { yearPickerDialog(2) }

        etAmount.setOnEditorActionListener { _, id, _ ->
            when (id) {
                EditorInfo.IME_ACTION_DONE -> calc(true)
            }
            false
        }
    }

    private fun calc(hasInputAmt: Boolean) {
        val amt = etAmount.text.toString()
        when {
            amt.isNotEmpty() && amt != "." -> {
                val meta = MetaBuilder().build(prefs.getYear1(), prefs.getYear2(), amt.toDouble())
                tvOutput.text = meta[0]
                tvBreakdown.text = meta[1]
                resetChart()
                initChart(amt.toDouble())
            }
            else -> when {
                hasInputAmt -> Toast.makeText(this, "please enter an amount", Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun yearPickerDialog(n: Int) {
        val builder = AlertDialog.Builder(this@MainActivity)
        val checkedItem = if (n == 1) prefs.getYear1() else prefs.getYear2()
        builder.setTitle("Select A Year")
        builder.setSingleChoiceItems(yearArray, checkedItem) { dialog, i ->
            when (n) {
                1 -> {
                    tvDateOne.text = yearArray[i]
                    prefs.setYear1(i)
                }
                else -> {
                    tvDateTwo.text = yearArray[i]
                    prefs.setYear2(i)
                }
            }
            calc(false)
            dialog.dismiss()
        }
        builder.setPositiveButton("Cancel") { dialog, _ -> dialog.cancel() }
        val mDialog = builder.create()
        mDialog.show()
    }

    private fun initChart(amt: Double) {

        barChart.visibility = View.VISIBLE
        val barDataSet = BarDataSet(ChartData(this).getData(amt), "inflation")

        barDataSet.apply {
            valueFormatter = CustomValueFormatter()
            color = ContextCompat.getColor(this@MainActivity, R.color.colorPrimary)
        }

        val dates = ChartData(this@MainActivity).getDates()
        val barData = BarData(barDataSet)

        barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            labelRotationAngle = 90f
            labelCount = dates.size
            granularity = 1f
            valueFormatter = DatesFormatter(dates)
            setDrawAxisLine(false)
            setDrawGridLines(false)
        }

        barChart.apply {
            data = barData
            axisLeft.setDrawGridLines(false)
            axisRight.isEnabled = false
            axisLeft.isEnabled = false
            description.isEnabled = false
            legend.isEnabled = true
            setVisibleXRangeMaximum(10f)
            moveViewToX(dates.size.toFloat())
            invalidate()
        }
    }

    private fun resetChart() {
        barChart.apply {
            fitScreen()
            data?.clearValues()
            xAxis.valueFormatter = null
            notifyDataSetChanged()
            clear()
            invalidate()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> {
                displayAboutDialog(this@MainActivity)
                true
            }
            R.id.action_share -> {
                share(tvBreakdown.text.toString(), this@MainActivity)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}