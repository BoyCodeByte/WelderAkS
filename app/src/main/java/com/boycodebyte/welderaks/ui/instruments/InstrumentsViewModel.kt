package com.boycodebyte.welderaks.ui.instruments

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boycodebyte.welderaks.data.models.Instrument
import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.data.repositories.InstrumentRepository
import com.boycodebyte.welderaks.data.repositories.ProfileRepository
import com.boycodebyte.welderaks.data.storage.FirebaseStorage
import com.boycodebyte.welderaks.domain.models.ErrorResult
import com.boycodebyte.welderaks.domain.models.PendingResult
import com.boycodebyte.welderaks.domain.models.SuccessResult
import com.boycodebyte.welderaks.domain.usecase.DeleteInstrumentByIDUseCase
import com.boycodebyte.welderaks.domain.usecase.GetInstrumentsUseCase
import com.boycodebyte.welderaks.domain.usecase.GetProfilesUseCase

class InstrumentsViewModel : ViewModel() {

    private val instrumentsUseCase=GetInstrumentsUseCase(InstrumentRepository(FirebaseStorage()))
    private val deleteInstrumentByIDUseCase=DeleteInstrumentByIDUseCase(InstrumentRepository(FirebaseStorage()))
    private val getProfilesUseCase=GetProfilesUseCase(ProfileRepository(FirebaseStorage()))

    private val _instrumentLiveData = MutableLiveData<List<Instrument>>()
    val instrumentLiveData: LiveData<List<Instrument>> = _instrumentLiveData

    private val _profileLiveData = MutableLiveData<List<Profile>>()
    val profileLiveData: LiveData<List<Profile>> = _profileLiveData

    fun updateInstrumentsList(){
        _instrumentLiveData.value = instrumentsUseCase.execute()
    }

    fun updateProfilesList(){
        _profileLiveData.value = getProfilesUseCase.execute()
    }

    fun deleteInstrument(instrument: Instrument){
        deleteInstrumentByIDUseCase.execute(instrument.id)
        updateInstrumentsList()
    }


    override fun onCleared() {
        super.onCleared()
    }


}