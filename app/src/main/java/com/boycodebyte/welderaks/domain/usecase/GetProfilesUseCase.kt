package com.boycodebyte.welderaks.domain.usecase

import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.data.repositories.ProfileRepository
import com.boycodebyte.welderaks.domain.models.PendingResult
import com.boycodebyte.welderaks.domain.models.SuccessResult
import com.boycodebyte.welderaks.domain.models.UiResult


class GetProfilesUseCase(private val repository: ProfileRepository) {
    fun execute(): List<Profile> {
        return repository.getProfilesList()
    }
}