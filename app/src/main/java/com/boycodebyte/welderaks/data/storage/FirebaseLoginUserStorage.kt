package com.boycodebyte.welderaks.data.storage

import com.boycodebyte.welderaks.data.models.LoginParam
import com.boycodebyte.welderaks.data.models.LoginUser
import com.boycodebyte.welderaks.data.models.RegisterParam
import com.google.firebase.database.FirebaseDatabase

const val CHILD = "login_users"

class FirebaseLoginUserStorage {
    fun getLoginUserList(): Result<List<LoginUser>> {
        return TODO()
    }

    fun addLoginUser(param: RegisterParam) {
        val myRef = FirebaseDatabase.getInstance().reference
        myRef.child(CHILD).child(param.login)
            .updateChildren(
                mapOf(
                    "login" to param.login,
                    "password" to param.password,
                    "id" to param.id
                )
            )
    }
}