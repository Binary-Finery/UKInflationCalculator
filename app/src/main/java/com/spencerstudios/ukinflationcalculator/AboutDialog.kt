package com.spencerstudios.ukinflationcalculator

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.widget.TextView

@SuppressLint("InflateParams")
fun displayAboutDialog(ctx : Context){
    val builder = AlertDialog.Builder(ctx)
    val v = LayoutInflater.from(ctx).inflate(R.layout.about_dialog, null)
    builder.setView(v)
    val version = v.findViewById<TextView>(R.id.version)
    version.text = "version ${BuildConfig.VERSION_NAME}"
    builder.setPositiveButton("Close"){dialog, _ ->
        dialog.dismiss()
    }
    val dialog = builder.create()
    dialog.show()
}