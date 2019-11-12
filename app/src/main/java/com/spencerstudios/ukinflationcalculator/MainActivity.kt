package com.spencerstudios.ukinflationcalculator

import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
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
        tvDateOne.setOnClickListener { yearPickerDialog(1) }
        tvDateTwo.setOnClickListener { yearPickerDialog(2) }
    }

    private fun calc() {

        val v1 = PrefUtils(this).getYear1()
        val v2 = PrefUtils(this).getYear2()

        if (etAmount.text.isNotEmpty() && etAmount.text.toString() != ".") {
            val amt = etAmount.text.toString().toDouble()
            val meta = MetaBuilder().build(v1, v2, amt)
            tvOutput.text = meta[0]
            tvBreakdown.text = meta[1]
        } else Toast.makeText(this, "please enter an amount", Toast.LENGTH_LONG).show()
    }

    private fun yearPickerDialog(n: Int) {
        val mBuilder = AlertDialog.Builder(this@MainActivity)
        val utils = PrefUtils(this@MainActivity)
        val checkedItem = if (n == 1) utils.getYear1() else utils.getYear2()
        mBuilder.setTitle("Select A Year")
        mBuilder.setSingleChoiceItems(yearArray, checkedItem) { dialog, i ->

            if (n == 1) {
                tvDateOne.text = yearArray[i]
                utils.setYear1(i)
            } else {
                tvDateTwo.text = yearArray[i]
                utils.setYear2(i)
            }

            dialog.dismiss()
        }
        mBuilder.setPositiveButton("Cancel") { dialog, _ -> dialog.cancel() }
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
            R.id.action_settings -> {
                displayAboutDialog(this@MainActivity)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}