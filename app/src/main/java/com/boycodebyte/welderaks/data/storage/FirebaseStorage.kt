package com.boycodebyte.welderaks.data.storage

import com.boycodebyte.welderaks.data.exceptions.InstrumentRequestException
import com.boycodebyte.welderaks.data.exceptions.ProfileRequestException
import com.boycodebyte.welderaks.data.models.AccountType
import com.boycodebyte.welderaks.data.models.Instrument
import com.boycodebyte.welderaks.data.models.LoginUser
import com.boycodebyte.welderaks.data.models.Profile
import com.google.firebase.database.FirebaseDatabase


const val PROFILES_CHILD = "profiles"
const val INSTRUMENTS_CHILD = "instruments"

//child of PROFILES_CHILD
const val LOGIN_CHILD = "login"
const val PASSWORD_CHILD = "password"
const val NAME_CHILD = "name"
const val SURNAME_CHILD = "surname"
const val DATE_OF_BIRTH_CHILD = "date_of_birth"
const val JOB_TITLE_CHILD = "job_title"
const val ACCOUNT_TYPE_CHILD = "account_type"
const val PHONE_NUMBER_CHILD = "phone_number"

//child of INSTRUMENTS_CHILD
const val DESCRIPTION_CHILD = "description"
const val ID_PROFILE_CHILD = "id_profile"




typealias LoginUsersCallback = (List<LoginUser>) -> Unit

typealias InstrumentsCallback = (List<Instrument>) -> Unit

class FirebaseStorage {

    fun getProfilesList(): List<Profile> {
        val myRef = FirebaseDatabase.getInstance().reference
        val request = myRef.child(PROFILES_CHILD).get()
        while (!request.isComplete) {
        }
        val profilesList = mutableListOf<Profile>()
        if (request.isSuccessful) {
            val snapshotList = request.result.children
            for (userChild in snapshotList) {
                val profile = Profile(
                    id = userChild.key.toString().toInt(),
                    login = userChild.child(LOGIN_CHILD).value.toString(),
                    password = userChild.child(PASSWORD_CHILD).value.toString(),
                    name = userChild.child(NAME_CHILD).value.toString(),
                    surname = userChild.child(SURNAME_CHILD).value.toString(),
                    dateOfBirth = userChild.child(DATE_OF_BIRTH_CHILD).value.toString(),
                    jobTitle = userChild.child(JOB_TITLE_CHILD).value.toString(),
                    accountType = AccountType.valueOf(userChild.child(ACCOUNT_TYPE_CHILD).value.toString()),
                    phoneNumber = userChild.child(PHONE_NUMBER_CHILD).value.toString()
                )
                profilesList.add(profile)
            }
        } else {
            throw ProfileRequestException()
        }
        return profilesList
    }

    fun addProfile(profile: Profile) {
        val myRef = FirebaseDatabase.getInstance().reference
        myRef.child(PROFILES_CHILD).child(profile.id.toString())
            .updateChildren(
                mapOf(
                    LOGIN_CHILD to profile.login,
                    PASSWORD_CHILD to profile.password,
                    NAME_CHILD to profile.name,
                    SURNAME_CHILD to profile.surname,
                    DATE_OF_BIRTH_CHILD to profile.dateOfBirth,
                    JOB_TITLE_CHILD to profile.jobTitle,
                    ACCOUNT_TYPE_CHILD to profile.accountType.name,
                    PHONE_NUMBER_CHILD to profile.phoneNumber,
                )
            )
    }

    fun removeProfile(id: Int) {
        val myRef = FirebaseDatabase.getInstance().reference
        myRef.child(PROFILES_CHILD).child(id.toString()).removeValue()
    }

    fun getInstrumentsList(): List<Instrument> {
        val myRef = FirebaseDatabase.getInstance().reference
        val request = myRef.child(INSTRUMENTS_CHILD).get()
        while (!request.isComplete) {
        }
        val instrumentList = mutableListOf<Instrument>()
        if (request.isSuccessful) {
            val childList = request.result.children
            for (instrumentChild in childList) {
                val instrument = Instrument(
                    id = instrumentChild.key.toString().toInt(),
                    name = instrumentChild.child(NAME_CHILD).value.toString(),
                    description = instrumentChild.child(DESCRIPTION_CHILD).value.toString(),
                    idOfProfile = instrumentChild.child(ID_PROFILE_CHILD).value.toString().toInt()
                )
                instrumentList.add(instrument)
            }
        } else {
            throw InstrumentRequestException()
        }
        return instrumentList
    }

    fun addInstrument(profile: Profile) {
        val myRef = FirebaseDatabase.getInstance().reference
        myRef.child(INSTRUMENTS_CHILD).child(profile.id.toString())
            .updateChildren(
                mapOf(
                    NAME_CHILD to profile.name,
                    SURNAME_CHILD to profile.surname,
                    DATE_OF_BIRTH_CHILD to profile.dateOfBirth,
                    JOB_TITLE_CHILD to profile.jobTitle,
                )
            )
    }

    fun removeInstrument(id: Int) {
        val myRef = FirebaseDatabase.getInstance().reference
        myRef.child(INSTRUMENTS_CHILD).child(id.toString()).removeValue()
    }
}