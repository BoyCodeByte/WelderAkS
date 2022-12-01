package com.boycodebyte.welderaks.data.repositories

import com.boycodebyte.welderaks.data.exceptions.InstrumentRequestException
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

    fun upDateDetails(instrument: Instrument){
        storage.upDateDetailsInstrument(instrument)
    }

    fun getInstrumentById(id: Int):Instrument{
            val instrument=storage.getInstrumentById(id)?:throw InstrumentRequestException()
        return instrument
    }
}