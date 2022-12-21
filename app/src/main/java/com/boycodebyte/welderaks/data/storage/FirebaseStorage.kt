package com.boycodebyte.welderaks.data.storage

import android.icu.util.Calendar
import com.boycodebyte.welderaks.data.exceptions.InstrumentRequestException
import com.boycodebyte.welderaks.data.exceptions.ProfileRequestException
import com.boycodebyte.welderaks.data.models.AccountType
import com.boycodebyte.welderaks.data.models.Instrument
import com.boycodebyte.welderaks.data.models.LoginUser
import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.data.models.CalendarData
import com.google.firebase.database.FirebaseDatabase


const val PROFILES_CHILD = "profiles"
const val INSTRUMENTS_CHILD = "instruments"
const val CALENDARS_CHILD = "calendars"

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

//child of CALENDARS_CHILD
const val YEARS_CHILD = "years"
const val MONTHS_CHILD = "months"
const val DAYS_CHILD = "days"
const val PREPAYMENT_CHILD = "prepayment"
const val SALARY_CHILD = "salary"
const val AWARD_CHILD = "award"
const val RATE_CHILD = "rate"
const val COEFFICIENT_CHILD = "coefficient"
const val HOURS_CHILD = "hours"




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


    //    //////////////////////////////////////////////////////
    fun addInstrument(instrument: Instrument) {
        val myRef = FirebaseDatabase.getInstance().reference
        myRef.child(INSTRUMENTS_CHILD).child(instrument.id.toString())
            .updateChildren(
                mapOf(
                    DESCRIPTION_CHILD to instrument.description,
                    ID_PROFILE_CHILD to instrument.idOfProfile,
                    NAME_CHILD to instrument.name
                )
            )
    }

    fun removeInstrument(id: Int) {
        val myRef = FirebaseDatabase.getInstance().reference
        myRef.child(INSTRUMENTS_CHILD).child(id.toString()).removeValue()
    }

    fun upDateDetailsInstrument(instrument: Instrument) {
        val myRef = FirebaseDatabase.getInstance().reference
        myRef.child(INSTRUMENTS_CHILD).child(instrument.id.toString())
            .updateChildren(
                mapOf(
                    DESCRIPTION_CHILD to instrument.description,
                    NAME_CHILD to instrument.name,
                    ID_PROFILE_CHILD to instrument.idOfProfile
                )
            )
    }

    fun getInstrumentById(id: Int): Instrument {
        val instrument =
            getInstrumentsList().firstOrNull() { it.id == id } ?: throw InstrumentRequestException()
        return instrument
    }

    fun getCalendarData(id: Int): CalendarData {
        val data = CalendarData()
        val myRef = FirebaseDatabase.getInstance().reference
        val request = myRef.child(CALENDARS_CHILD).get()
        while (!request.isComplete) {
        }
        if (request.isSuccessful) {
            val yearsChild = request.result.child(id.toString()).child(YEARS_CHILD).children
            for (yearChild in yearsChild) {
                val year = CalendarData.Year(
                    number = yearChild.key.toString().toInt()
                )
                val monthsChild = yearChild.child(MONTHS_CHILD).children
                for (monthChild in monthsChild) {
                    val month = CalendarData.Month(
                        number = monthChild.key.toString().toInt()
                    )
                    month.prepayment = try {
                        monthChild.child(PREPAYMENT_CHILD).value.toString().toInt()
                    } catch (_: Exception) {
                        0
                    }
                    month.salary = try {
                        monthChild.child(SALARY_CHILD).value.toString().toInt()
                    } catch (_: Exception) {
                        0
                    }
                    month.award = try {
                        monthChild.child(AWARD_CHILD).value.toString().toInt()
                    } catch (_: Exception) {
                        0
                    }
                    val daysChild = monthChild.child(DAYS_CHILD).children
                    for (dayChild in daysChild) {
                        val day = CalendarData.Day(
                            number = dayChild.key.toString().toInt(),
                            rate = dayChild.child(RATE_CHILD).value.toString().toInt(),
                            coefficient = dayChild.child(COEFFICIENT_CHILD).value.toString()
                                .toDouble(),
                            hours = dayChild.child(HOURS_CHILD).value.toString().toInt(),
                            description = dayChild.child(DESCRIPTION_CHILD).value.toString()
                        )
                        month.days.add(day)
                    }
                    year.months.add(month)
                }
                data.years.add(year)
            }
        } else {
            throw InstrumentRequestException()
        }
        return data
    }

    fun getProfileById(id: Int): Profile {
        val profile =
            getProfilesList().firstOrNull() { it.id == id } ?: throw ProfileRequestException()
        return profile
    }

    fun setDayData(id: Int, date: Calendar, day: CalendarData.Day) {
        val myRef = FirebaseDatabase.getInstance().reference
        myRef.child(CALENDARS_CHILD).child(id.toString())
            .child(YEARS_CHILD)
            .child(date.get(Calendar.YEAR).toString())
            .child(MONTHS_CHILD)
            .child((date.get(Calendar.MONTH) + 1).toString())
            .child(DAYS_CHILD)
            .child(date.get(Calendar.DATE).toString())
            .updateChildren(
                mapOf(
                    DESCRIPTION_CHILD to day.description,
                    RATE_CHILD to day.rate,
                    HOURS_CHILD to day.hours,
                    COEFFICIENT_CHILD to day.coefficient
                )
            )

    }

    fun setPrepaymentData(id: Int, date: Calendar, prepayment: Int) {
        val myRef = FirebaseDatabase.getInstance().reference
        myRef.child(CALENDARS_CHILD).child(id.toString())
            .child(YEARS_CHILD)
            .child(date.get(Calendar.YEAR).toString())
            .child(MONTHS_CHILD)
            .child((date.get(Calendar.MONTH) + 1).toString())
            .child(PREPAYMENT_CHILD).setValue(prepayment)
    }
    fun setSalaryData(id: Int, date: Calendar, salary: Int) {
        val myRef = FirebaseDatabase.getInstance().reference
        myRef.child(CALENDARS_CHILD).child(id.toString())
            .child(YEARS_CHILD)
            .child(date.get(Calendar.YEAR).toString())
            .child(MONTHS_CHILD)
            .child((date.get(Calendar.MONTH) + 1).toString())
            .child(SALARY_CHILD).setValue(salary)
    }
    fun setAwardData(id: Int, date: Calendar, award: Int) {
        val myRef = FirebaseDatabase.getInstance().reference
        myRef.child(CALENDARS_CHILD).child(id.toString())
            .child(YEARS_CHILD)
            .child(date.get(Calendar.YEAR).toString())
            .child(MONTHS_CHILD)
            .child((date.get(Calendar.MONTH) + 1).toString())
            .child(AWARD_CHILD).setValue(award)
    }
}