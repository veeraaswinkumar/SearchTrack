package com.searchtracks.extension

import android.text.TextUtils
import android.util.Patterns
import java.util.concurrent.TimeUnit

fun String.convertToInteger(value: String?): Int {
    var result = 0
    try {
        if (value != null && value.trim { it <= ' ' } != "") {
            result = Integer.parseInt(value.trim { it <= ' ' })
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return result
}

fun String.convertToDouble(value: String?): Double {
    var result = 0.0
    try {
        if (value != null && value.trim { it <= ' ' } != "") {
            result = java.lang.Double.parseDouble(value.trim { it <= ' ' })
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }
    return result
}

fun String.convertToLong(value: String?): Long {
    var result: Long = 0
    try {
        if (value != null && value.trim { it <= ' ' } != "") {
            result = java.lang.Long.parseLong(value.trim { it <= ' ' })
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return result
}

fun String.convertToFloat(value: String?): Float {
    var result = 0f
    try {
        if (value != null && value.trim { it <= ' ' } != "") {
            result = java.lang.Float.parseFloat(value.trim { it <= ' ' })
        }
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return result
}

/**
 * to check valid url
 */
fun String.isValidUrl(): Boolean = this.isNotEmpty() && Patterns.WEB_URL.matcher(this).matches()

fun String.isValidEmail(): Boolean {
    return if (TextUtils.isEmpty(this.trim()))
        false
    else Patterns.EMAIL_ADDRESS.matcher(this.trim()).matches()
}

fun String.getTimeDifferent() : String {
    val mills = System.currentTimeMillis() - this.toLong()
    return TimeUnit.MILLISECONDS.toSeconds(mills).toString()
}

