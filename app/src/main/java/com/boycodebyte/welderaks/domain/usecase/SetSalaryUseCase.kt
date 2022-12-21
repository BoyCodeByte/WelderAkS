package com.boycodebyte.welderaks.domain.usecase

import android.icu.util.Calendar
import com.boycodebyte.welderaks.data.repositories.CalendarDataRepository

class SetSalaryUseCase (private val calendarDataRepository: CalendarDataRepository) {
    fun execute(id: Int, date: Calendar, salary: Int){
        calendarDataRepository.setSalaryData(id,date,salary)
    }
}