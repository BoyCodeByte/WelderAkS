package com.boycodebyte.welderaks.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.boycodebyte.welderaks.MainActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

class SplashScreenActivity : AppCompatActivity() {

    var job: Job? = null

    override fun onStart() {
        super.onStart()
        job = lifecycleScope.async{
            delay(3000)
            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    override fun onStop() {
        super.onStop()
        job?.cancel()
    }
}