package com.boycodebyte.welderaks.data.repositories

import android.icu.util.Calendar
import com.boycodebyte.welderaks.data.models.CalendarData
import com.boycodebyte.welderaks.data.storage.FirebaseStorage

class CalendarDataRepository(private val storage: FirebaseStorage) {
    fun getCalendarData(id: Int): CalendarData {
        return storage.getCalendarData(id)
    }

    fun setDayData(id: Int, date: Calendar, day: CalendarData.Day) {
        storage.setDayData(id, date, day)
    }
}