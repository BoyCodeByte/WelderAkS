package com.boycodebyte.welderaks.domain.usecase

import com.boycodebyte.welderaks.data.models.LoginParam
import com.boycodebyte.welderaks.data.repositories.LoginUsersRepository

class LoginUseCase(private val repository: LoginUsersRepository) {
    fun execute(param: LoginParam){
        val loginUserList = repository.getLoginUsersList()

    }
}