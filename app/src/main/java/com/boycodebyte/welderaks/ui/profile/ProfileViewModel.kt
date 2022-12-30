package com.boycodebyte.welderaks.ui.profile

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.data.repositories.LoginParamRepository
import com.boycodebyte.welderaks.data.storage.PrefStorage
import com.boycodebyte.welderaks.domain.usecase.RemoveLoginParamUseCase


class ProfileViewModel: ViewModel() {

    private val _profileLifeData = MutableLiveData<ProfileState>()
    val profileLiveData: LiveData<ProfileState> = _profileLifeData

    var profile: Profile? = null
    set(value) {
        field = value
        if (value != null) {
            val profileState = ProfileState(
                name = value.name,
                surname = value.surname,
                dateOfBirth = value.dateOfBirth,
                jobTitle = value.jobTitle,
                rate = value.rate.toString()
            )
            _profileLifeData.value = profileState
        }
    }

    fun logout(context: Context){
        val useCase = RemoveLoginParamUseCase(LoginParamRepository(PrefStorage(context)))
        useCase.execute()
    }



}