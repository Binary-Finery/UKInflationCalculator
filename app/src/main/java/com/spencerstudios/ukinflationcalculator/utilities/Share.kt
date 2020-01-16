package com.spencerstudios.ukinflationcalculator.utilities

import android.content.Context
import android.content.Intent

fun share(text : String, ctx : Context) {
    if (text.isNotEmpty()) {
        val i = Intent()
        i.apply {
            action = Intent.ACTION_SEND
            type = "text/plain"
            putExtra(Intent.EXTRA_TEXT, text)
        }
        ctx.startActivity(Intent.createChooser(i, "share to..."))
    }
}