package com.boycodebyte.welderaks.data.models

fun getEmptyProfile():Profile{return Profile(0,"","","",""
    ,"","",0,AccountType.WORKER,"")}


data class Profile(
    var id: Int,
    var login: String,
    var password: String,
    var name: String,
    var surname: String,
    var dateOfBirth: String,
    var jobTitle: String,
    var rate: Int = 0,
    var accountType: AccountType,
    var phoneNumber: String
){
    override fun toString(): String {
        return "$name $surname"
    }
}