package com.boycodebyte.welderaks.ui.instruments.general.detailsinstrument

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boycodebyte.welderaks.data.exceptions.InstrumentRequestException
import com.boycodebyte.welderaks.data.models.Instrument
import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.data.models.getEmptyProfile
import com.boycodebyte.welderaks.data.repositories.InstrumentRepository
import com.boycodebyte.welderaks.data.repositories.ProfileRepository
import com.boycodebyte.welderaks.data.storage.FirebaseStorage
import com.boycodebyte.welderaks.domain.usecase.GetInstrumentByIdUseCase
import com.boycodebyte.welderaks.domain.usecase.GetProfilesUseCase
import com.boycodebyte.welderaks.domain.usecase.UpdateDetailsInstrumentUseCase

class InstrumentDetailsViewModel : ViewModel() {

    private val updateDetails =
        UpdateDetailsInstrumentUseCase(InstrumentRepository(FirebaseStorage()))
    private val getInstrumentByIdUseCase =
        GetInstrumentByIdUseCase(InstrumentRepository(FirebaseStorage()))


    private val _instrumentDetails = MutableLiveData<Instrument>()
    val instrumentDetails: LiveData<Instrument> = _instrumentDetails

    private var profiles = GetProfilesUseCase(ProfileRepository((FirebaseStorage()))).execute()
    private val _profile = MutableLiveData<List<Profile>>()
    val profile: LiveData<List<Profile>> = _profile

    init {
        profiles=ArrayList<Profile>().apply {
            add(getEmptyProfile())
        addAll(profiles)
        }
        _profile.value=profiles
    }

    fun loadInstrument(instrumentId: Int) {
        if (_instrumentDetails.value != null) return
        try {
            _instrumentDetails.value = getInstrumentByIdUseCase.execute(instrumentId)
        } catch (e: InstrumentRequestException) {
            e.printStackTrace()
        }
    }

    fun saveChange(numberProfile:Int,description:String) {
        val instrument = this.instrumentDetails.value ?: return
        instrument.idOfProfile=profiles.get(numberProfile).id
        instrument.description=description
        updateDetails.execute(instrument)
    }
}