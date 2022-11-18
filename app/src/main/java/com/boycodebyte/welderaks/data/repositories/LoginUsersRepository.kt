package com.boycodebyte.welderaks.data.repositories

import com.boycodebyte.welderaks.data.models.LoginUser
import com.boycodebyte.welderaks.data.models.RegisterParam
import com.boycodebyte.welderaks.data.storage.LoginUsersCallback
import com.boycodebyte.welderaks.data.storage.FirebaseStorage

class LoginUsersRepository(private val storage: FirebaseStorage) {

    fun getLoginUsersList(loginUsersCallback: LoginUsersCallback){
       storage.getLoginUsersList(loginUsersCallback)
    }

    fun addLoginUser(param: LoginUser) {
        storage.addLoginUser(param)
    }

    fun removeLoginUser(id: Int){
        storage.removeLoginUser(id)
    }

}