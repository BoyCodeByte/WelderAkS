package com.boycodebyte.welderaks.data.repositories

import com.boycodebyte.welderaks.data.models.LoginUser
import com.boycodebyte.welderaks.data.models.RegisterParam
import com.boycodebyte.welderaks.data.storage.FirebaseStorage

class LoginUsersRepository(private val storage: FirebaseStorage) {

    fun getLoginUsersList(): List<LoginUser> {
        return storage.getLoginUsersList()
    }

    fun addLoginUser(param: RegisterParam) {
        storage.addLoginUser(param)
    }

    fun removeLoginUser(user: LoginUser){
        storage.removeLoginUser(user)
    }

}