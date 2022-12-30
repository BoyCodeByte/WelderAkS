package com.boycodebyte.welderaks.domain.usecase

import com.boycodebyte.welderaks.data.repositories.LoginParamRepository

class RemoveLoginParamUseCase(private val repository: LoginParamRepository) {
    fun execute(){
        return repository.removeLoginParam()
    }
}