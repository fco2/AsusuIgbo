package com.asusuigbo.frank.asusuigbo.helpers

import java.util.*

class DateHelper {
    companion object{
        fun getFormattedDate(): String {
            val cal = Calendar.getInstance()
            val fmt = Formatter()
            cal.set(Calendar.MONTH, cal.get(Calendar.MONTH))
            val year = cal.get(Calendar.YEAR)
            val monthName = fmt.format("%tB", cal)
            return "$monthName, $year"
        }
    }
}