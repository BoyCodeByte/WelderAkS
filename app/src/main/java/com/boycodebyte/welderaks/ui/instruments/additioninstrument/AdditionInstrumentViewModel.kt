package com.boycodebyte.welderaks.ui.instruments.additioninstrument

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boycodebyte.welderaks.data.models.Instrument
import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.data.models.getEmptyProfile
import com.boycodebyte.welderaks.data.repositories.InstrumentRepository
import com.boycodebyte.welderaks.data.repositories.ProfileRepository
import com.boycodebyte.welderaks.data.storage.FirebaseStorage
import com.boycodebyte.welderaks.domain.usecase.AdditionInstrumentUseCase
import com.boycodebyte.welderaks.domain.usecase.GetProfilesUseCase

class AdditionInstrumentViewModel:ViewModel() {

    private val addingInstrumentUseCase=AdditionInstrumentUseCase(InstrumentRepository(FirebaseStorage()))
    private var profiles=GetProfilesUseCase(ProfileRepository(FirebaseStorage())).execute()
    private val _profile = MutableLiveData<List<Profile>>()
    val profile:LiveData<List<Profile>> = _profile


    init {
        profiles=ArrayList<Profile>().apply {
            add(getEmptyProfile())
            addAll(profiles)
        }
        _profile.value=profiles
    }

    fun addition(numberProfile:Int,description:String,nameInstrument:String){
        val instrument = Instrument()
        instrument.idOfProfile=profiles.get(numberProfile).id
        instrument.description=description
        instrument.name=nameInstrument
        instrument.id=4
        addingInstrumentUseCase.execute(instrument)
    }

}