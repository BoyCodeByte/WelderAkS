package com.boycodebyte.welderaks.data.repositories

import com.boycodebyte.welderaks.data.exceptions.LoginException
import com.boycodebyte.welderaks.data.models.LoginParam
import com.boycodebyte.welderaks.data.models.LoginUser
import com.boycodebyte.welderaks.data.storage.FirebaseLoginUserStorage

class LoginUserRepository(private val storage: FirebaseLoginUserStorage) {

    fun getLoginUser(param: LoginParam): LoginUser {
        val loginUser = storage.login(param)
        if(loginUser == null){
            throw LoginException()
        } else {
            return loginUser
        }
    }

    fun addLoginUser(param: LoginParam) {
        storage.register(param)
    }

}