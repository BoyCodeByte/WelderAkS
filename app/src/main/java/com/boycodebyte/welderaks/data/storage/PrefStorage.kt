package com.boycodebyte.welderaks.data.storage

import android.content.Context
import com.boycodebyte.welderaks.data.exceptions.LoginParamException
import com.boycodebyte.welderaks.data.models.LoginParam

private const val AUTH_PREF = "auth_pref"
private const val LOGIN_PARAM = "login_param"
private const val PASSWORD_PARAM = "password_param"

class PrefStorage(private val context: Context) {


    fun getLoginParam(): LoginParam {
        val sharedPreferences =
            context.getSharedPreferences(AUTH_PREF, Context.MODE_PRIVATE) ?: throw LoginParamException()
        val login = sharedPreferences.getString(LOGIN_PARAM, null)
        val password = sharedPreferences.getString(PASSWORD_PARAM, null)
        return if (login != null && password != null) {
            return LoginParam(login, password)
        } else {
            throw LoginParamException()
        }
    }

    fun saveLoginParam(param: LoginParam) {
        val sharedPreferences =
            context.getSharedPreferences(AUTH_PREF, Context.MODE_PRIVATE)?: throw LoginParamException()
        sharedPreferences.edit().putString(LOGIN_PARAM, param.login).apply()
        sharedPreferences.edit().putString(PASSWORD_PARAM, param.password).apply()
    }

    fun removeLoginParam(){
        val sharedPreferences =
            context.getSharedPreferences(AUTH_PREF, Context.MODE_PRIVATE)?: throw LoginParamException()
        sharedPreferences.edit().remove(LOGIN_PARAM).apply()
        sharedPreferences.edit().remove(PASSWORD_PARAM).apply()
    }
}