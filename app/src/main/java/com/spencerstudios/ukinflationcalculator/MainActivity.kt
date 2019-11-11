package com.spencerstudios.ukinflationcalculator

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity;
import android.view.Menu
import android.view.MenuItem
import android.widget.TextView
import android.widget.Toast

import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var yearArray: Array<String?>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        yearArray = foo()

        val prefs = PrefUtils(this)
        tvDateOne.text = yearArray[prefs.getYear1()]
        tvDateTwo.text = yearArray[prefs.getYear2()]

        btnCalc.setOnClickListener { calc() }
        tvDateOne.setOnClickListener { yearPickerDialog(tvDateOne) }
        tvDateTwo.setOnClickListener { yearPickerDialog(tvDateTwo) }
    }

    private fun calc() {
        val val1 = tvDateOne.text.toString().toInt()
        val val2 = tvDateTwo.text.toString().toInt()

        if (etAmount.text.isNotEmpty()) {
            val amt = etAmount.text.toString().toDouble()
            val meta = MetaBuilder().build(val1, val2, amt)
            tvOutput.text = meta[0]
            tvBreakdown.text = meta[1]
        } else Toast.makeText(this, "please enter an amount", Toast.LENGTH_LONG).show()
    }

    private fun yearPickerDialog(tv: TextView) {
        val mBuilder = AlertDialog.Builder(this@MainActivity)
        mBuilder.setTitle("Select A Year")
        mBuilder.setSingleChoiceItems(yearArray, 1) { dialog, i ->
            tv.text = yearArray[i]
            dialog.dismiss()
        }
        mBuilder.setNeutralButton("Cancel") { dialog, _ -> dialog.cancel() }
        val mDialog = mBuilder.create()
        mDialog.show()
    }

    private fun foo(): Array<String?> {
        val meta = MetaBuilder().buildYearMeta()
        val array = arrayOfNulls<String>(meta.size)
        (0 until meta.size).forEach { i ->
            array[i] = "${meta[i].year}"
        }
        return array
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }
}