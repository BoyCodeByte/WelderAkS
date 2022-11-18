package com.boycodebyte.welderaks.data.repositories

import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.data.storage.FirebaseStorage
import com.boycodebyte.welderaks.data.storage.InstrumentsCallback

class InstrumentRepository(private val storage: FirebaseStorage) {

    fun getInstrumentsList(instrumentsCallback: InstrumentsCallback) {
       storage.getInstrumentsList(instrumentsCallback)
    }

    fun addInstrument(profile: Profile) {
       storage.addInstrument(profile)
    }

    fun removeInstrument(id: Int) {
        storage.removeInstrument(id)
    }
}