package com.boycodebyte.welderaks.domain.usecase

import com.boycodebyte.welderaks.data.repositories.InstrumentRepository



class DeleteInstrumentByIDUseCase(private val repository: InstrumentRepository) {
    fun execute(id:Int){
        repository.removeInstrument(id)
    }
}