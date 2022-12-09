package com.boycodebyte.welderaks.domain.usecase

import com.boycodebyte.welderaks.data.models.CalendarData
import com.boycodebyte.welderaks.data.repositories.CalendarDataRepository

class GetCalendarDataByIDUseCase(private val repository: CalendarDataRepository) {
    fun execute(id: Int): CalendarData {
        return repository.getCalendarData(id)
    }
}