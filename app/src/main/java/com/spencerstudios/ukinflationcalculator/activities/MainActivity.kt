package com.spencerstudios.ukinflationcalculator.activities

import android.content.Intent
import android.os.Bundle
import android.support.v4.content.ContextCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.spencerstudios.ukinflationcalculator.R
import com.spencerstudios.ukinflationcalculator.dialogs.displayAboutDialog
import com.spencerstudios.ukinflationcalculator.formatters.CustomValueFormatter
import com.spencerstudios.ukinflationcalculator.formatters.DatesFormatter
import com.spencerstudios.ukinflationcalculator.utilities.MetaBuilder
import com.spencerstudios.ukinflationcalculator.utilities.PrefUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlin.math.max
import kotlin.math.min

class MainActivity : AppCompatActivity() {

    private lateinit var lineChartView : LineChart
    private lateinit var yearArray: Array<String?>
    private lateinit var prefs: PrefUtils


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        lineChartView = findViewById(R.id.chart)

        prefs = PrefUtils(this)
        yearArray = MetaBuilder().getYearArray()

        tvDateOne.text = yearArray[prefs.getYear1()]
        tvDateTwo.text = yearArray[prefs.getYear2()]

        btnCalc.setOnClickListener { calc() }
        tvDateOne.setOnClickListener { yearPickerDialog(1) }
        tvDateTwo.setOnClickListener { yearPickerDialog(2) }
    }

    private fun calc() {
        if (etAmount.text.isNotEmpty() && etAmount.text.toString() != ".") {
            val amt = etAmount.text.toString().toDouble()
            val meta = MetaBuilder()
                .build(prefs.getYear1(), prefs.getYear2(), amt)
            tvOutput.text = meta[0]
            tvBreakdown.text = meta[1]
            resetChart()
            initChart(amt)
        } else Toast.makeText(this, "please enter an amount", Toast.LENGTH_LONG).show()
    }

    private fun yearPickerDialog(n: Int) {
        val builder = AlertDialog.Builder(this@MainActivity)
        val checkedItem = if (n == 1) prefs.getYear1() else prefs.getYear2()
        builder.setTitle("Select A Year")
        builder.setSingleChoiceItems(yearArray, checkedItem) { dialog, i ->
            if (n == 1) {
                tvDateOne.text = yearArray[i]
                prefs.setYear1(i)
            } else {
                tvDateTwo.text = yearArray[i]
                prefs.setYear2(i)
            }
            dialog.dismiss()
        }
        builder.setPositiveButton("Cancel") { dialog, _ -> dialog.cancel() }
        val mDialog = builder.create()
        mDialog.show()
    }


    private fun share() {
        val text = tvBreakdown.text
        if (text.isNotEmpty()) {
            val i = Intent()
            i.action = Intent.ACTION_SEND
            i.type = "text/plain"
            i.putExtra(Intent.EXTRA_TEXT, text)
            startActivity(Intent.createChooser(i, "share to..."))
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
                share()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun getData(amt : Double): ArrayList<Entry> {
        val data = ArrayList<Entry>()
        val yo = prefs.getYear1()
        val yt = prefs.getYear2()
        val ymin = min(yo, yt)
        val ymax = max(yo, yt)

        val meta = MetaBuilder().buildYearMeta()

        for((idx, i) in (ymin..ymax).withIndex())
            data.add(Entry(idx.toFloat(), ((meta[i].value / meta[yo].value) * amt).toFloat()))
        return data
    }

    private fun resetChart(){
        lineChartView.apply {
            fitScreen()
            data?.clearValues()
            xAxis.valueFormatter = null
            notifyDataSetChanged()
            clear()
            invalidate()
        }
    }

    private fun initChart(amt : Double) {
        val lineColor = ContextCompat.getColor(this, R.color.colorPrimary)
        val lineDataSet = LineDataSet(getData(amt), "inflation")
        lineDataSet.apply {
            setCircleColor(lineColor)
            color = lineColor
        }

        val dates = MetaBuilder().getDates(this@MainActivity)

        val data = LineData(lineDataSet)
        lineChartView.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            labelRotationAngle = 90f
            setDrawAxisLine(true)
            labelCount = dates.size
            setAvoidFirstLastClipping(true)
            axisMaximum = data.xMax + .5f
            axisMinimum = data.xMin - .5f
            granularity = 1f
            valueFormatter = DatesFormatter(dates)
        }

        data.setValueFormatter(CustomValueFormatter())
        lineChartView.data = data

        lineChartView.apply {
            axisRight.isEnabled = false
            axisLeft.isEnabled = false
            description.isEnabled = false
            setVisibleXRangeMaximum(10f)
            legend.isEnabled = true
            moveViewToX(dates.size.toFloat())
            invalidate()
        }
    }
}