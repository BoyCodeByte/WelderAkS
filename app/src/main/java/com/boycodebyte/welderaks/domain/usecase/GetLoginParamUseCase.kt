package com.boycodebyte.welderaks.domain.usecase

import com.boycodebyte.welderaks.data.models.LoginParam
import com.boycodebyte.welderaks.data.repositories.LoginParamRepository

class GetLoginParamUseCase(private val repository: LoginParamRepository) {
    fun execute(): LoginParam {
        return repository.getLoginParam()
    }
}