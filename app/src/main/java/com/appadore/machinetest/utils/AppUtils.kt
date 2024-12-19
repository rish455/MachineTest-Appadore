package com.appadore.machinetest.utils

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import com.appadore.machinetest.R
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale


object AppUtils {

    fun showProgressDialog(context: Context): AlertDialog? {
        val builder = AlertDialog.Builder(context)
        builder.setCancelable(false)
        builder.setView(R.layout.layout_blocking_progress)
        return builder.create()
    }

    interface ActionAlertCallBack {
        fun positiveButton(dialogInterface: DialogInterface)
        fun negativeButton(dialogInterface: DialogInterface)
    }

    fun convertMillisToTimeFormat(timeInMillis: Long): String {
        val date = Date(timeInMillis)
        val timeFormat = SimpleDateFormat("hh:mm:ss a", Locale.getDefault())
        return timeFormat.format(date)
    }



    inline fun <reified T : Parcelable> Intent.getParcelableExtraCompat(key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelableExtra(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            getParcelableExtra(key)
        }
    }


    inline fun <reified T : Parcelable> Bundle.getParcelableCompat(key: String): T? {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            getParcelable(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            getParcelable(key)
        }
    }
}