package com.boycodebyte.welderaks.domain.usecase

import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.data.repositories.ProfileRepository

class UpdateDetailsProfileUseCase(private val repository: ProfileRepository) {
    fun execute(profile: Profile){
        repository.addProfile(profile)
    }
}