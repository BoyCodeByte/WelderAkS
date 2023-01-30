package com.boycodebyte.welderaks.domain.usecase


import com.boycodebyte.welderaks.data.repositories.SystemRepository

class VersionControlUseCase(private val repository: SystemRepository) {
    fun execute(uvid: String): Boolean {
        return repository.isUvidEquals(uvid)
    }
}