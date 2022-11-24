package com.boycodebyte.welderaks.data.repositories

import com.boycodebyte.welderaks.data.models.Instrument
import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.data.storage.FirebaseStorage

class InstrumentRepository(private val storage: FirebaseStorage) {

    fun getInstrumentsList(): List<Instrument> {
       return storage.getInstrumentsList()
    }

    fun addInstrument(profile: Profile) {
       storage.addInstrument(profile)
    }

    fun removeInstrument(id: Int) {
        storage.removeInstrument(id)
    }
}