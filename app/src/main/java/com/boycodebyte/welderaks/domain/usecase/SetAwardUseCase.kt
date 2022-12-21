package com.boycodebyte.welderaks.domain.usecase

import android.icu.util.Calendar
import com.boycodebyte.welderaks.data.repositories.CalendarDataRepository

class SetAwardUseCase (private val calendarDataRepository: CalendarDataRepository) {
    fun execute(id: Int, date: Calendar, award: Int){
        calendarDataRepository.setAwardData(id,date,award)
    }
}