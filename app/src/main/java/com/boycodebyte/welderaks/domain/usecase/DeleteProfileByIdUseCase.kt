package com.boycodebyte.welderaks.domain.usecase

import com.boycodebyte.welderaks.data.repositories.ProfileRepository

class DeleteProfileByIdUseCase(val repository: ProfileRepository) {
    fun execute(id:Int){
        repository.removeProfile(id)
    }
}