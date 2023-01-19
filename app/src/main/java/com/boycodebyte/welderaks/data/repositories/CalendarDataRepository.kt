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

    fun removeDayData(id: Int, date: Calendar){
        storage.removeDayData(id,date)
    }

    fun setPrepaymentData(id: Int, date: Calendar, prepayment: Int) {
        storage.setPrepaymentData(id, date, prepayment)
    }

    fun setSalaryData(id: Int, date: Calendar, salary: Int) {
        storage.setSalaryData(id, date, salary)
    }

    fun setAwardData(id: Int, date: Calendar, award: Int) {
        storage.setAwardData(id, date, award)
    }
}