package com.boycodebyte.welderaks.domain.usecase

import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.data.repositories.ProfileRepository
import kotlin.math.absoluteValue
import kotlin.random.Random

class AdditionProfileUseCase(private val repository: ProfileRepository) {
    fun execute(profile: Profile){
        val ids=repository.getProfilesList().map { it.id }
        var random=0
        do {
            random= Random.nextInt().absoluteValue
        }while (ids.contains(random))
        profile.id=random
        repository.addProfile(profile)
    }
}