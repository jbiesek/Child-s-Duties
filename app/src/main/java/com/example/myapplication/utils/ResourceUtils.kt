package com.example.myapplication.utils

import android.content.Context

object ResourceUtils {
    fun getResourceDrawableId(drawableName: String?, context: Context): Int {
        return context.resources.getIdentifier(drawableName,"drawable", context.packageName)
    }
}