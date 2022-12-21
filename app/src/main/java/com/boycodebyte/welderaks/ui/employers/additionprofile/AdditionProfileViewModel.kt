package com.boycodebyte.welderaks.ui.employers.additionprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.boycodebyte.welderaks.data.models.AccountType
import com.boycodebyte.welderaks.data.models.getEmptyProfile
import com.boycodebyte.welderaks.data.repositories.ProfileRepository
import com.boycodebyte.welderaks.data.storage.FirebaseStorage
import com.boycodebyte.welderaks.domain.usecase.AdditionProfileUseCase


class AdditionProfileViewModel: ViewModel() {

    private val additionProfileUseCase=AdditionProfileUseCase(ProfileRepository(FirebaseStorage()))

    private var type:List<String> = ArrayList<String>().apply {
        add(AccountType.GENERAL.toString())
        add(AccountType.MASTER.toString())
        add(AccountType.WORKER.toString())
    }
    private val _accountType= MutableLiveData<List<String>>()
    val accountType:LiveData<List<String>> = _accountType

    init {
        _accountType.value=type
    }

    fun add(name:String, surname:String, accountType: String, dateOfBirth:String,
        jobTitle:String, login:String, password:String, phoneNumber:String,rate: Int){

        println(accountType)
        val profile= getEmptyProfile()
        when(accountType){
            "GENERAL"->profile.accountType=AccountType.GENERAL
            "MASTER"->profile.accountType=AccountType.MASTER
            "WORKER"->profile.accountType=AccountType.WORKER
        }
        profile.name=name
        profile.surname=surname
        profile.dateOfBirth=dateOfBirth
        profile.jobTitle=jobTitle
        profile.login=login
        profile.password=password
        profile.phoneNumber=phoneNumber
        profile.rate=rate
        additionProfileUseCase.execute(profile)
    }
}