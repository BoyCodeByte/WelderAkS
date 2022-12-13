package com.boycodebyte.welderaks.domain.usecase

import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.data.repositories.ProfileRepository

class GetProfileByIdUseCase(private val repository: ProfileRepository) {
    fun execute(id:Int):Profile{
        return repository.getProfileById(id)
    }
}