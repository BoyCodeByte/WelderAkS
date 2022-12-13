package com.boycodebyte.welderaks.ui.employers.detailsprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boycodebyte.welderaks.data.exceptions.ProfileRequestException
import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.data.models.getEmptyProfile
import com.boycodebyte.welderaks.data.repositories.ProfileRepository
import com.boycodebyte.welderaks.data.storage.FirebaseStorage
import com.boycodebyte.welderaks.domain.usecase.GetProfileByIdUseCase
import com.boycodebyte.welderaks.domain.usecase.GetProfilesUseCase
import com.boycodebyte.welderaks.domain.usecase.UpdateDetailsProfileUseCase

class ProfileDetailsViewModel: ViewModel() {

    private val updateDetailsProfileUseCase=UpdateDetailsProfileUseCase(ProfileRepository(FirebaseStorage()))
    private var profileByIdUseCase = GetProfileByIdUseCase(ProfileRepository(FirebaseStorage()))
    private val _profile = MutableLiveData<Profile>()
    val profile: LiveData<Profile> = _profile

    fun loadProfile(id:Int){
        if (_profile.value!=null) return
        try {
            _profile.value= profileByIdUseCase.execute(id)
        }catch (e: ProfileRequestException){
            e.printStackTrace()
        }
    }

    fun saveChange(name:String, surname:String, dateOfBirth:String, jobTitle:String, phoneNumber:String){
        val prof=this.profile.value?:return
        prof.name=name
        prof.surname=surname
        prof.jobTitle=jobTitle
        prof.dateOfBirth=dateOfBirth
        prof.phoneNumber=phoneNumber
        updateDetailsProfileUseCase.execute(prof)
    }
}