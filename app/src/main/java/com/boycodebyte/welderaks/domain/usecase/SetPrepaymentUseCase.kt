package com.boycodebyte.welderaks.domain.usecase

import android.icu.util.Calendar
import com.boycodebyte.welderaks.data.repositories.CalendarDataRepository

class SetPrepaymentUseCase (private val calendarDataRepository: CalendarDataRepository) {
    fun execute(id: Int, date: Calendar, prepayment: Int){
        calendarDataRepository.setPrepaymentData(id,date,prepayment)
    }
}