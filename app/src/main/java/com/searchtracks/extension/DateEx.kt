package com.searchtracks.extension

import java.text.SimpleDateFormat
import java.util.*

fun String.getCurrentDateTimeInFormat(): String {
    val format = SimpleDateFormat(this,Locale.US)
    val c = Calendar.getInstance()
    return format.format(c.time)
}


fun String.formatLocalDateTimeFromString(Pattern: String, Default: String): String {
    var returnDate = Default
    if (this.equals("0000-00-00 00:00:00", ignoreCase = true) || this.equals("", ignoreCase = true)) {
        return returnDate
    }
    try {
        var format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US)
        val newDate = format.parse(this)
        format = SimpleDateFormat(Pattern,Locale.US)
        format.timeZone = TimeZone.getDefault()
        returnDate = format.format(newDate!!)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return returnDate
}


fun String.formatLocalDateFromString(Pattern: String, Default: String): String {
    var returnDate = Default
    if (this.equals("00/00/0000", ignoreCase = true) || this.equals("", ignoreCase = true)) {
        return returnDate
    }
    try {
        var format = SimpleDateFormat("MM/dd/yyyy",Locale.US)
        val newDate = format.parse(this)
        format = SimpleDateFormat(Pattern,Locale.US)
        format.timeZone = TimeZone.getDefault()
        returnDate = format.format(newDate!!)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return returnDate
}

fun String.formatLocalTimeFromString(Pattern: String, Default: String): String {
    var returnDate = Default
    if (this.equals("00-00-00", ignoreCase = true) || this.equals("", ignoreCase = true)) {
        return returnDate
    }
    try {
        var format = SimpleDateFormat("HH:mm:ss",Locale.US)
        val newDate = format.parse(this)
        format = SimpleDateFormat(Pattern,Locale.US)
        format.timeZone = TimeZone.getDefault()
        returnDate = format.format(newDate!!)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return returnDate
}

fun String.formatUTCDateTimeToLocal(Pattern: String, Default: String): String {
    var returnDate = Default
    if (this.equals("0000-00-00 00:00:00", ignoreCase = true) || this.equals("", ignoreCase = true)) {
        return returnDate
    }
    try {
        var format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.ENGLISH)
        format.timeZone = TimeZone.getTimeZone("UTC")
        val newDate = format.parse(this)
        format = SimpleDateFormat(Pattern,Locale.US)
        format.timeZone = TimeZone.getDefault()
        returnDate = format.format(newDate!!)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return returnDate
}


fun String.formatLocalDateTimeToUTC(Pattern: String, Default: String): String {
    var returnDate = Default
    if (this.equals("0000-00-00 00:00:00", ignoreCase = true) || this.equals("", ignoreCase = true)) {
        return returnDate
    }
    try {
        var format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss",Locale.US)
        val newDate = format.parse(this)
        format = SimpleDateFormat(Pattern,Locale.US)
        format.timeZone = TimeZone.getTimeZone("UTC")
        returnDate = format.format(newDate!!)
    } catch (e: Exception) {
        e.printStackTrace()
    }

    return returnDate
}

