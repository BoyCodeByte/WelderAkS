package com.boycodebyte.welderaks.data.repositories

import com.boycodebyte.welderaks.data.models.CalendarData
import com.boycodebyte.welderaks.data.storage.FirebaseStorage

class CalendarDataRepository(private val storage: FirebaseStorage) {
    fun getCalendarData(id: Int): CalendarData {
        return storage.getCalendarData(id)
    }
}