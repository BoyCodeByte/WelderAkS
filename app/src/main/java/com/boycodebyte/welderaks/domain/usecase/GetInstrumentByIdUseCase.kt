package com.boycodebyte.welderaks.domain.usecase

import com.boycodebyte.welderaks.data.exceptions.InstrumentRequestException
import com.boycodebyte.welderaks.data.models.Instrument
import com.boycodebyte.welderaks.data.repositories.InstrumentRepository

class GetInstrumentByIdUseCase(private val repository: InstrumentRepository) {
    fun execute(id:Int):Instrument{
        return repository.getInstrumentById(id)
    }
}