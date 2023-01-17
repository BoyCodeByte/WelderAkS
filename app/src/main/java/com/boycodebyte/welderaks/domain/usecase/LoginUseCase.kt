package com.boycodebyte.welderaks.domain.usecase

import com.boycodebyte.welderaks.data.models.LoginParam
import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.data.repositories.ProfileRepository
import com.boycodebyte.welderaks.domain.exception.AuthenticationException


class LoginUseCase(private val repository: ProfileRepository) {
    fun execute(param: LoginParam): Profile {
        if(param.login.isEmpty() || param.password.isEmpty()){
            throw AuthenticationException()
        }
        val profileList = repository.getProfilesList()
        var currentProfile: Profile? = null
        for (profile in profileList) {
            if (profile.login == param.login
                && profile.password == param.password
            ) {
                currentProfile = profile
                break
            }
        }
        if (currentProfile != null) {
            return currentProfile
        } else {
            throw AuthenticationException()
        }
    }
}