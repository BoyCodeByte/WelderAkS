package com.boycodebyte.welderaks.domain.usecase

import com.boycodebyte.welderaks.data.models.LoginParam
import com.boycodebyte.welderaks.data.repositories.LoginParamRepository

class SetLoginParamUseCase(private val repository: LoginParamRepository) {
    fun execute(param : LoginParam ) {
        repository.saveLoginParam(param)
    }
}