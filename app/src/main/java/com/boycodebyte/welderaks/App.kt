package com.boycodebyte.welderaks

import android.app.Activity
import android.app.Application
import com.boycodebyte.welderaks.data.models.Profile

class App: Application() {
    var profile: Profile? = null
}

fun Activity.getProfile(): Profile?{
    val app = application as App
    return app.profile
}
fun Activity.setProfile(profile: Profile){
    val app = application as App
    app.profile = profile
}