package com.boycodebyte.welderaks.data.storage

import com.boycodebyte.welderaks.data.models.LoginParam
import com.boycodebyte.welderaks.data.models.LoginUser
import com.google.firebase.database.FirebaseDatabase

const val CHILD = "login_users"

class FirebaseLoginUserStorage {
    fun login(param: LoginParam): LoginUser? {
        return TODO()
    }

    fun register(param: LoginParam) {
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