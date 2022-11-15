package com.boycodebyte.welderaks.domain.usecase

import com.boycodebyte.welderaks.data.models.*
import com.boycodebyte.welderaks.data.repositories.LoginUsersRepository
import com.boycodebyte.welderaks.domain.models.ErrorResult
import com.boycodebyte.welderaks.domain.models.PendingResult
import com.boycodebyte.welderaks.domain.models.SuccessResult
import com.boycodebyte.welderaks.domain.models.LoginResult
import java.lang.Exception

typealias LoginCallback = (LoginResult<LoginUser>) -> Unit

class LoginUseCase(private val repository: LoginUsersRepository) {
    fun execute(param: LoginParam, callback: LoginCallback) {
        callback.invoke(PendingResult())
        repository.getLoginUsersList { loginUsersList ->
            var currentLoginUser: LoginUser? = null
            for(loginUser in loginUsersList){
                if (loginUser.login == param.login
                    && loginUser.password == param.password){
                    currentLoginUser = loginUser
                    break
                }
            }
            if(currentLoginUser != null) {
                callback.invoke(SuccessResult(currentLoginUser))
            } else {
                callback.invoke(ErrorResult(Exception()))
            }
        }

    }
}