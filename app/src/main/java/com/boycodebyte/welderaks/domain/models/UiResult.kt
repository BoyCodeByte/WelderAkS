package com.boycodebyte.welderaks.domain.models

sealed class UiResult<T>

class PendingResult<T> : UiResult<T>()
class SuccessResult<T>(val data: T) : UiResult<T>()
class ErrorResult<T>(val exception: Exception) : UiResult<T>()