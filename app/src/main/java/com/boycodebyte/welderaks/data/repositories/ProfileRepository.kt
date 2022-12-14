package com.boycodebyte.welderaks.data.repositories

import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.data.storage.FirebaseStorage

class ProfileRepository(private val storage: FirebaseStorage) {
    fun getProfilesList(): List<Profile>{
        return storage.getProfilesList()
    }
    fun addProfile(profile: Profile){
        storage.addProfile(profile)
    }
    fun removeProfile(id: Int){
        storage.removeProfile(id)
    }

    fun getProfileById(id: Int):Profile{
        val profile=storage.getProfileById(id)
        return profile
    }
}