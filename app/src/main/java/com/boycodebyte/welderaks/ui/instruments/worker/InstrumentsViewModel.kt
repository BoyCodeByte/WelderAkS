package com.boycodebyte.welderaks.ui.instruments.worker

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boycodebyte.welderaks.data.models.Instrument
import com.boycodebyte.welderaks.data.repositories.InstrumentRepository
import com.boycodebyte.welderaks.data.storage.FirebaseStorage
import com.boycodebyte.welderaks.domain.usecase.GetInstrumentsByUserIdUseCase

class InstrumentsViewModel : ViewModel() {

    private val instrumentsByUserIdUseCase= GetInstrumentsByUserIdUseCase(InstrumentRepository(FirebaseStorage()))

    private val _instrumentLiveData = MutableLiveData<List<Instrument>>()
    val instrumentLiveData: LiveData<List<Instrument>> = _instrumentLiveData

    fun updateInstrumentsList(id: Int){
        _instrumentLiveData.value = instrumentsByUserIdUseCase.execute(id)
    }
}