package com.boycodebyte.welderaks.data.models

import com.boycodebyte.welderaks.data.storage.LOGIN_CHILD
import com.boycodebyte.welderaks.data.storage.PASSWORD_CHILD

data class LoginUser(val login: String,
                     val password: String,
                     val id: Int)
