package com.boycodebyte.welderaks.data.repositories

import com.boycodebyte.welderaks.data.models.LoginParam
import com.boycodebyte.welderaks.data.storage.PrefStorage

class LoginParamRepository(private val storage: PrefStorage) {

    fun getLoginParam(): LoginParam {
        return storage.getLoginParam()
    }

    fun saveLoginParam(param: LoginParam) {
        storage.saveLoginParam(param)
    }

    fun removeLoginParam(){
        storage.removeLoginParam()
    }
}