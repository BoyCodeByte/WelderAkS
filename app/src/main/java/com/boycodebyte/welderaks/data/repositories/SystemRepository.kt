package com.boycodebyte.welderaks.data.repositories

import com.boycodebyte.welderaks.data.storage.FirebaseStorage

class SystemRepository(private val storage: FirebaseStorage) {
    fun isUvidEquals(uvid: String): Boolean{
        return storage.isUvidEquals(uvid)
    }
}