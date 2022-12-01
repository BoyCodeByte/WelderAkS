package com.boycodebyte.welderaks.domain.usecase

import com.boycodebyte.welderaks.data.models.Instrument
import com.boycodebyte.welderaks.data.repositories.InstrumentRepository

class UpdateDetailsInstrumentUseCase(private val repository: InstrumentRepository) {
    fun execute(instrument: Instrument){
        repository.upDateDetails(instrument)
    }
}