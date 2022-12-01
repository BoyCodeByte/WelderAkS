package com.boycodebyte.welderaks.data.models

fun getEmptyProfile():Profile{return Profile(0,"","","",""
    ,"","",AccountType.WORKER,"")}


data class Profile(
    val id: Int,
    val login: String,
    val password: String,
    val name: String,
    val surname: String,
    val dateOfBirth: String,
    val jobTitle: String,
    val accountType: AccountType,
    val phoneNumber: String
){
    override fun toString(): String {
        return "$name $surname"
    }
}