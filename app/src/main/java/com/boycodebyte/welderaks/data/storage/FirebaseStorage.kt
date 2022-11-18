package com.boycodebyte.welderaks.data.storage

import com.boycodebyte.welderaks.data.models.Instrument
import com.boycodebyte.welderaks.data.models.LoginUser
import com.boycodebyte.welderaks.data.models.Profile
import com.google.firebase.database.FirebaseDatabase

const val LOGIN_USERS_CHILD = "login_users"
const val PROFILES_CHILD = "profiles"
const val INSTRUMENTS_CHILD = "instruments"

//child of LOGIN_USER_CHILD
const val LOGIN_CHILD = "login"
const val PASSWORD_CHILD = "password"

//child of PROFILES_CHILD
const val NAME_CHILD = "name"
const val SURNAME_CHILD = "lastname"
const val DATE_OF_BIRTH_CHILD = "date_of_birth"
const val JOB_TITLE_CHILD = "job_title"

//child of INSTRUMENTS_CHILD
const val DESCRIPTION_CHILD = "description"
const val ID_PROFILE_CHILD = "id_profile"




typealias LoginUsersCallback = (List<LoginUser>) -> Unit
typealias ProfilesCallback = (List<Profile>) -> Unit
typealias InstrumentsCallback = (List<Instrument>) -> Unit

class FirebaseStorage {

    fun getLoginUsersList(loginUsersCallback: LoginUsersCallback) {
        val myRef = FirebaseDatabase.getInstance().reference
        myRef.child(LOGIN_USERS_CHILD).get().addOnSuccessListener {
            val snapshotList = it.children
            val loginUsersList = mutableListOf<LoginUser>()
            for (userChild in snapshotList) {
                val id = userChild.key.toString().toInt()
                val login = userChild.child(LOGIN_CHILD).value.toString()
                val password = userChild.child(PASSWORD_CHILD).value.toString()
                val loginUser = LoginUser(login, password, id)
                loginUsersList.add(loginUser)
            }
            loginUsersCallback.invoke(loginUsersList)

        }
    }

    fun addLoginUser(loginUser: LoginUser) {
        val myRef = FirebaseDatabase.getInstance().reference
        myRef.child(LOGIN_USERS_CHILD).child(loginUser.id.toString())
            .updateChildren(
                mapOf(
                    LOGIN_CHILD to loginUser.login,
                    PASSWORD_CHILD to loginUser.password,
                )
            )
    }

    fun removeLoginUser(id: Int) {

    }

    fun getProfilesList(profilesCallback: ProfilesCallback) {
        val myRef = FirebaseDatabase.getInstance().reference
        myRef.child(PROFILES_CHILD).get().addOnSuccessListener {
            val snapshotList = it.children
            val profilesList = mutableListOf<Profile>()
            for (userChild in snapshotList) {
                val profile = Profile(
                    id = userChild.key.toString().toInt(),
                    name = userChild.child(SURNAME_CHILD).value.toString(),
                    surname = userChild.child(DATE_OF_BIRTH_CHILD).value.toString(),
                    dateOfBirth = userChild.child(DATE_OF_BIRTH_CHILD).value.toString(),
                    jobTitle = userChild.child(JOB_TITLE_CHILD).value.toString()
                )
                profilesList.add(profile)
            }
            profilesCallback.invoke(profilesList)
        }
    }

    fun addProfile(profile: Profile) {
        val myRef = FirebaseDatabase.getInstance().reference
        myRef.child(LOGIN_USERS_CHILD).child(profile.id.toString())
            .updateChildren(
                mapOf(
                    NAME_CHILD to profile.name,
                    SURNAME_CHILD to profile.surname,
                    DATE_OF_BIRTH_CHILD to profile.dateOfBirth,
                    JOB_TITLE_CHILD to profile.jobTitle,
                )
            )
    }

    fun removeProfile(id: Int) {

    }

    fun getInstrumentsList(instrumentsCallback: InstrumentsCallback) {
        val myRef = FirebaseDatabase.getInstance().reference
        myRef.child(INSTRUMENTS_CHILD).get().addOnSuccessListener {
            val snapshotList = it.children
            val profilesList = mutableListOf<Instrument>()
            for (userChild in snapshotList) {
                val instrument = Instrument(
                    id = userChild.key.toString().toInt(),
                    name = userChild.child(SURNAME_CHILD).value.toString(),
                    description = userChild.child(DATE_OF_BIRTH_CHILD).value.toString(),
                    idOfProfile = userChild.child(DATE_OF_BIRTH_CHILD).value.toString().toInt()
                )
                profilesList.add(instrument)
            }
            instrumentsCallback.invoke(profilesList)
        }
    }

    fun addInstrument(profile: Profile) {
        val myRef = FirebaseDatabase.getInstance().reference
        myRef.child(LOGIN_USERS_CHILD).child(profile.id.toString())
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

    }
}