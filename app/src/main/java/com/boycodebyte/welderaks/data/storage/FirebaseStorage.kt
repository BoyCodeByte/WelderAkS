package com.boycodebyte.welderaks.data.storage

import com.boycodebyte.welderaks.data.exceptions.FireBaseException
import com.boycodebyte.welderaks.data.models.LoginUser
import com.boycodebyte.welderaks.data.models.RegisterParam
import com.google.firebase.database.FirebaseDatabase

const val LOGIN_USERS_CHILD = "login_users"
const val LOGIN_CHILD = "login"
const val PASSWORD_CHILD = "password"
const val ID_CHILD = "id"

class FirebaseStorage {

    fun getLoginUsersList(): List<LoginUser> {
        val myRef = FirebaseDatabase.getInstance().reference
        val task = myRef.child(LOGIN_USERS_CHILD).get()
        if (task.isSuccessful){
            val snapshotList = task.result.children
            val loginUsersList = mutableListOf<LoginUser>()
            for(userChild in snapshotList){
                val login = userChild.child(LOGIN_CHILD).value.toString()
                val password = userChild.child(PASSWORD_CHILD).value.toString()
                val id = userChild.child(ID_CHILD).value.toString().toInt()
                val loginUser = LoginUser(login, password, id)
                loginUsersList.add(loginUser)
            }
            return loginUsersList
        }else {
            throw FireBaseException()
        }
//        myRef.child(LOGIN_USERS_CHILD).get().addOnSuccessListener {
//            val snapshotList = it.children
//            val loginUsersList = mutableListOf<LoginUser>()
//            for(userChild in snapshotList){
//                val login = userChild.child(LOGIN_CHILD).value.toString()
//                val password = userChild.child(PASSWORD_CHILD).value.toString()
//                val id = userChild.child(ID_CHILD).value.toString().toInt()
//                val loginUser = LoginUser(login, password, id)
//                loginUsersList.add(loginUser)
//            }
//        }
    }

    fun addLoginUser(param: RegisterParam) {
        val myRef = FirebaseDatabase.getInstance().reference
        myRef.child(LOGIN_USERS_CHILD).child(param.login)
            .updateChildren(
                mapOf(
                    LOGIN_CHILD to param.login,
                    PASSWORD_CHILD to param.password,
                    ID_CHILD to param.id
                )
            )
    }

    fun removeLoginUser(user: LoginUser){

    }
}