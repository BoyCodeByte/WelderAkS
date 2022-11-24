package com.boycodebyte.welderaks.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.boycodebyte.welderaks.MainActivity
import com.boycodebyte.welderaks.data.repositories.LoginParamRepository
import com.boycodebyte.welderaks.data.repositories.ProfileRepository
import com.boycodebyte.welderaks.data.storage.FirebaseStorage
import com.boycodebyte.welderaks.data.storage.PrefStorage
import com.boycodebyte.welderaks.domain.usecase.GetLoginParamUseCase
import com.boycodebyte.welderaks.domain.usecase.LoginUseCase
import com.boycodebyte.welderaks.setProfile
import com.boycodebyte.welderaks.ui.login.LoginActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

class SplashScreenActivity : AppCompatActivity() {

    private var job: Job? = null
    private val getLoginParamRepository =
        GetLoginParamUseCase(LoginParamRepository((PrefStorage(this))))
    private val loginUseCase = LoginUseCase(ProfileRepository(FirebaseStorage()))
    override fun onStart() {
        super.onStart()
        job = lifecycleScope.async {
            delay(2000)
            try {
                val params = getLoginParamRepository.execute()
                val profile = loginUseCase.execute(params)
                setProfile(profile)
                val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                startActivity(intent)
                finish()
            } catch (e: Exception) {
                val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                startActivity(intent)
                finish()
            }
        }
    }

    override fun onStop() {
        super.onStop()
        job?.cancel()
    }
}