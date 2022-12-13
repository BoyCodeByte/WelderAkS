package com.boycodebyte.welderaks.domain.usecase

import com.boycodebyte.welderaks.data.models.Instrument
import com.boycodebyte.welderaks.data.repositories.InstrumentRepository
import kotlin.math.absoluteValue
import kotlin.random.Random
import kotlin.random.nextUInt

class AdditionInstrumentUseCase(private val repository: InstrumentRepository) {
    fun execute(instrument: Instrument){
        val ids=repository.getInstrumentsList().map { it.id }
        var randomId=0
        do {
            randomId= Random.nextInt().absoluteValue
        }while (ids.contains(randomId))
        instrument.id=randomId
        repository.addInstrument(instrument)
    }
}