package com.labs64.netlicensing.util

import java.util.Calendar
import java.util.TimeZone
import javax.xml.bind.DatatypeConverter

object DateUtils {
    fun getCurrentDate(): Calendar {
        return Calendar.getInstance(TimeZone.getTimeZone("UTC"))
    }

    fun parseDate(dateTime: String): Calendar {
        val dateTimeCl = DatatypeConverter.parseDateTime(dateTime)
        dateTimeCl.timeZone = TimeZone.getTimeZone("UTC")
        return dateTimeCl
    }

    fun printDate(date: Calendar): String {
        date.timeZone = TimeZone.getTimeZone("UTC")
        return DatatypeConverter.printDateTime(date)
    }
}