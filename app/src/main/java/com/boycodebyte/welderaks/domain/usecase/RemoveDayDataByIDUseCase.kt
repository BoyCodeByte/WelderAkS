package com.boycodebyte.welderaks.domain.usecase

import android.icu.util.Calendar
import com.boycodebyte.welderaks.data.repositories.CalendarDataRepository

class RemoveDayDataByIDUseCase(private val repository: CalendarDataRepository) {
    fun execute(id: Int, date: Calendar) {
        return repository.removeDayData(id, date)
    }
}
