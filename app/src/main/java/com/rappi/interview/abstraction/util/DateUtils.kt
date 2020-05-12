package com.rappi.interview.abstraction.util

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    const val FULL_DATE_PATTERN = "dd MMMM yyyy"
    const val NORMAL_DATE_PATTERN = "yyyy-MM-dd"

    @JvmStatic
    fun format(date: Date?, format: String): String {
        val datFormatter = SimpleDateFormat(format, Locale.getDefault())
        datFormatter.timeZone = TimeZone.getDefault()
        return datFormatter.format(date!!)
    }
}