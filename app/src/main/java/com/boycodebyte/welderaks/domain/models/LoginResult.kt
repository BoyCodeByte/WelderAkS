package com.boycodebyte.welderaks.domain.models

sealed class LoginResult<T>

class PendingResult<T> : LoginResult<T>()
class SuccessResult<T>(val data: T) : LoginResult<T>()
class ErrorResult<T>(val exception: Exception) : LoginResult<T>()