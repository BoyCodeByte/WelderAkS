package com.boycodebyte.welderaks.data.repositories

import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.data.storage.FirebaseStorage
import com.boycodebyte.welderaks.data.storage.ProfilesCallback

class ProfileRepository(private val storage: FirebaseStorage) {
    fun getProfilesList(callback: ProfilesCallback){
        storage.getProfilesList(callback)
    }
    fun addProfile(profile: Profile){
        storage.addProfile(profile)
    }
    fun removeProfile(id: Int){
        storage.removeProfile(id)
    }
}