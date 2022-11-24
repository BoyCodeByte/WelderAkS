package com.boycodebyte.welderaks.domain.usecase

import com.boycodebyte.welderaks.data.models.Instrument
import com.boycodebyte.welderaks.data.repositories.InstrumentRepository

class GetInstrumentsUseCase(private val repository: InstrumentRepository) {
    fun execute(): List<Instrument> {
        return repository.getInstrumentsList()
    }
}