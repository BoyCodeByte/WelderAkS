package com.boycodebyte.welderaks.domain.usecase

import com.boycodebyte.welderaks.data.models.Instrument
import com.boycodebyte.welderaks.data.repositories.InstrumentRepository

class GetInstrumentsByUserIdUseCase(private val repository: InstrumentRepository) {
    fun execute(id: Int): List<Instrument> {
        val instruments = repository.getInstrumentsList()
        return instruments.filter { it.idOfProfile == id }
    }
}
