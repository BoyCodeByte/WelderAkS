package com.boycodebyte.welderaks.domain.usecase

import com.boycodebyte.welderaks.data.models.Instrument
import com.boycodebyte.welderaks.data.models.LoginUser
import com.boycodebyte.welderaks.data.repositories.InstrumentRepository
import com.boycodebyte.welderaks.data.repositories.LoginUsersRepository
import com.boycodebyte.welderaks.domain.models.ErrorResult
import com.boycodebyte.welderaks.domain.models.PendingResult
import com.boycodebyte.welderaks.domain.models.SuccessResult
import com.boycodebyte.welderaks.domain.models.UiResult
import java.lang.Exception

typealias InstrumentCallback = (UiResult<List<Instrument>>) -> Unit

class GetInstrumentsUseCase(private val repository: InstrumentRepository) {
    fun execute(callback: InstrumentCallback) {
        callback.invoke(PendingResult())
        repository.getInstrumentsList { instrumentList ->
            callback.invoke(SuccessResult(instrumentList))
        }
    }
}