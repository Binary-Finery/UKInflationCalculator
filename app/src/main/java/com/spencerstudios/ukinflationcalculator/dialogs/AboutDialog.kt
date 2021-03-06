package com.spencerstudios.ukinflationcalculator.dialogs

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView
import com.spencerstudios.ukinflationcalculator.BuildConfig
import com.spencerstudios.ukinflationcalculator.R

@SuppressLint("InflateParams", "SetTextI18n")
fun displayAboutDialog(ctx : Context){
    val builder = AlertDialog.Builder(ctx)
    val view = LayoutInflater.from(ctx).inflate(R.layout.about_dialog, null)
    builder.setView(view)
    val version = view.findViewById<TextView>(R.id.version)
    version.text = "version ${BuildConfig.VERSION_NAME}"
    builder.setPositiveButton("Close"){dialog, _ -> dialog.dismiss() }
    builder.create().show()
}