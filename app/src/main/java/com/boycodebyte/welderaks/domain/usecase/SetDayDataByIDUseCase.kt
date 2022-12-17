package com.boycodebyte.welderaks.domain.usecase

import android.icu.util.Calendar
import com.boycodebyte.welderaks.data.models.CalendarData
import com.boycodebyte.welderaks.data.repositories.CalendarDataRepository

class SetDayDataByIDUseCase(private val calendarDataRepository: CalendarDataRepository) {
    fun execute(id: Int, date: Calendar, day: CalendarData.Day){
        calendarDataRepository.setDayData(id,date,day)
    }

}
