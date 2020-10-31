package com.asusuigbo.frank.asusuigbo.helpers

import java.util.*

object Constants {
    const val DATABASE_NAME = "asusu_igbo_database"

    const val PICK_IMAGE_REQUEST_CODE = 1

    fun getBeginningOfWeekDate(): String{
        val cal = Calendar.getInstance()
        cal[Calendar.HOUR_OF_DAY] = 0
        cal.clear(Calendar.MINUTE)
        cal.clear(Calendar.SECOND)
        cal.clear(Calendar.MILLISECOND)
        // get start of this week in milliseconds
        cal[Calendar.DAY_OF_WEEK] = cal.firstDayOfWeek
        val fmt = Formatter()
        val mthName = fmt.format("%tB", cal)
        //ex. January 12, 2020
        return "$mthName ${cal.get(Calendar.DAY_OF_MONTH)}, ${cal.get(Calendar.YEAR)}"
    }
}