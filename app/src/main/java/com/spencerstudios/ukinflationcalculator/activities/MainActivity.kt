package com.spencerstudios.ukinflationcalculator.activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import com.spencerstudios.ukinflationcalculator.R
import com.spencerstudios.ukinflationcalculator.dialogs.displayAboutDialog
import com.spencerstudios.ukinflationcalculator.utilities.MetaBuilder
import com.spencerstudios.ukinflationcalculator.utilities.PrefUtils
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var yearArray: Array<String?>
    private lateinit var prefs : PrefUtils

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        prefs = PrefUtils(this)
        yearArray = getYearArray()

        tvDateOne.text = yearArray[prefs.getYear1()]
        tvDateTwo.text = yearArray[prefs.getYear2()]

        btnCalc.setOnClickListener { calc() }
        tvDateOne.setOnClickListener { yearPickerDialog(1) }
        tvDateTwo.setOnClickListener { yearPickerDialog(2) }
    }

    private fun calc() {
        if (etAmount.text.isNotEmpty() && etAmount.text.toString() != ".") {
            val meta = MetaBuilder()
                .build( prefs.getYear1(), prefs.getYear2(), etAmount.text.toString().toDouble())
            tvOutput.text = meta[0]
            tvBreakdown.text = meta[1]
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

    private fun getYearArray(): Array<String?> {
        val meta = MetaBuilder().buildYearMeta()
        val array = arrayOfNulls<String>(meta.size)
        (0 until meta.size).forEach { i ->
            array[i] = "${meta[i].year}"
        }
        return array
    }

    private fun share(){
        val text = tvBreakdown.text
        if(text.isNotEmpty()){
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
            R.id.action_share ->{
                share()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }
}