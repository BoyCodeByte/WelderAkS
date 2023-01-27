package com.boycodebyte.welderaks.ui.instruments.worker.detailsinstrument

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

    private val getInstrumentByIdUseCase =
        GetInstrumentByIdUseCase(InstrumentRepository(FirebaseStorage()))

    private val _instrumentDetails = MutableLiveData<Instrument>()
    val instrumentDetails: LiveData<Instrument> = _instrumentDetails

    private var profiles = GetProfilesUseCase(ProfileRepository((FirebaseStorage()))).execute()

    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile> = _profile

    fun loadInstrument(instrumentId: Int) {
        if (_instrumentDetails.value != null) return
        try {
            val instrument = getInstrumentByIdUseCase.execute(instrumentId)
            _profile.value = profiles.first { instrument.idOfProfile == it.id }
            _instrumentDetails.value = instrument

        } catch (e: InstrumentRequestException) {
            e.printStackTrace()
        }
    }
}