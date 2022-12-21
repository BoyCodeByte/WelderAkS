package com.boycodebyte.welderaks.ui.employers.detailsprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boycodebyte.welderaks.data.exceptions.ProfileRequestException
import com.boycodebyte.welderaks.data.models.AccountType
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

    private var type:List<String>
    private val _accountType= MutableLiveData<List<String>>()
    val accountType:LiveData<List<String>> = _accountType

    init {
        type=ArrayList<String>().apply {
            add(AccountType.GENERAL.toString())
            add(AccountType.MASTER.toString())
            add(AccountType.WORKER.toString())
        }
        _accountType.value=type
    }

    fun loadProfile(id:Int){
        if (_profile.value!=null) return
        try {
            _profile.value= profileByIdUseCase.execute(id)
        }catch (e: ProfileRequestException){
            e.printStackTrace()
        }
    }

    fun saveChange(name:String, surname:String, dateOfBirth:String, jobTitle:String, phoneNumber:String,
                   rate: Int, login: String, password: String, accountType: String){
        val prof=this.profile.value?:return

        when(accountType){
            "GENERAL"->prof.accountType=AccountType.GENERAL
            "MASTER"->prof.accountType=AccountType.MASTER
            "WORKER"->prof.accountType=AccountType.WORKER
        }

        prof.name=name
        prof.surname=surname
        prof.jobTitle=jobTitle
        prof.dateOfBirth=dateOfBirth
        prof.phoneNumber=phoneNumber
        prof.rate=rate
        prof.login=login
        prof.password=password
        updateDetailsProfileUseCase.execute(prof)
    }
}