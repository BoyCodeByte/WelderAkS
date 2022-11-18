package com.boycodebyte.welderaks.domain.usecase

import com.boycodebyte.welderaks.data.models.Profile
import com.boycodebyte.welderaks.data.repositories.ProfileRepository
import com.boycodebyte.welderaks.domain.models.PendingResult
import com.boycodebyte.welderaks.domain.models.SuccessResult
import com.boycodebyte.welderaks.domain.models.UiResult

typealias ProfileCallback = (UiResult<List<Profile>>) -> Unit
class GetProfilesUseCase(private val repository: ProfileRepository) {
    fun execute(callback: ProfileCallback) {
        callback.invoke(PendingResult())
        repository.getProfilesList { profilesList ->
            callback.invoke(SuccessResult(profilesList))
        }
    }

}