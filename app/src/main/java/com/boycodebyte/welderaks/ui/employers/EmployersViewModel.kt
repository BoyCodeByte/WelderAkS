package com.boycodebyte.welderaks.ui.employers

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.data.repositories.ProfileRepository
import com.boycodebyte.welderaks.data.storage.FirebaseStorage
import com.boycodebyte.welderaks.domain.usecase.DeleteProfileByIdUseCase
import com.boycodebyte.welderaks.domain.usecase.GetProfilesUseCase

class EmployersViewModel : ViewModel() {

    private val _profiles = MutableLiveData<List<Profile>>()
    val profiles: LiveData<List<Profile>> = _profiles

    val employers=GetProfilesUseCase(ProfileRepository(FirebaseStorage()))
    val deleteProfile=DeleteProfileByIdUseCase(ProfileRepository(FirebaseStorage()))

    fun updateProfileList(){
        _profiles.value=employers.execute()
    }

    fun deleteProfile(profile: Profile){
        deleteProfile.execute(profile.id)
        updateProfileList()
    }
}