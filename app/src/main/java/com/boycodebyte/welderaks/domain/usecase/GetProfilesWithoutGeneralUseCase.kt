package com.boycodebyte.welderaks.domain.usecase

import com.boycodebyte.welderaks.data.models.AccountType
import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.data.repositories.ProfileRepository
import com.boycodebyte.welderaks.domain.models.PendingResult
import com.boycodebyte.welderaks.domain.models.SuccessResult
import com.boycodebyte.welderaks.domain.models.UiResult


class GetProfilesWithoutGeneralUseCase(private val repository: ProfileRepository) {
    fun execute(): List<Profile> {
        val profiles = repository.getProfilesList()
        val list: ArrayList<Profile> = ArrayList()
        profiles.forEach{
            if(it.accountType!= AccountType.GENERAL){
                list.add(it)
            }
        }
        return list
    }
}