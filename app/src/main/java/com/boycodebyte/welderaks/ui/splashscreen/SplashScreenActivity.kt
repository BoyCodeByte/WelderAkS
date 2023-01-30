package com.boycodebyte.welderaks.ui.splashscreen

import android.content.Intent
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.boycodebyte.welderaks.GeneralActivity
import com.boycodebyte.welderaks.R
import com.boycodebyte.welderaks.WorkerActivity
import com.boycodebyte.welderaks.data.models.AccountType
import com.boycodebyte.welderaks.data.repositories.LoginParamRepository
import com.boycodebyte.welderaks.data.repositories.ProfileRepository
import com.boycodebyte.welderaks.data.repositories.SystemRepository
import com.boycodebyte.welderaks.data.storage.FirebaseStorage
import com.boycodebyte.welderaks.data.storage.PrefStorage
import com.boycodebyte.welderaks.domain.usecase.GetLoginParamUseCase
import com.boycodebyte.welderaks.domain.usecase.LoginUseCase
import com.boycodebyte.welderaks.domain.usecase.VersionControlUseCase
import com.boycodebyte.welderaks.setProfile
import com.boycodebyte.welderaks.ui.login.LoginActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

class SplashScreenActivity : AppCompatActivity() {

    private val UVID = "75b58181a76691t91"
    private var job: Job? = null
    private val versionControlUseCase = VersionControlUseCase(SystemRepository(FirebaseStorage()))
    private val getLoginParamUseCase =
        GetLoginParamUseCase(LoginParamRepository((PrefStorage(this))))
    private val loginUseCase = LoginUseCase(ProfileRepository(FirebaseStorage()))
    override fun onStart() {
        super.onStart()
        job = lifecycleScope.async {
            delay(2000)
            try {
                if(!versionControlUseCase.execute(UVID)){
                    val toast = Toast.makeText(
                        this@SplashScreenActivity,
                        getString(R.string.old_version),
                        Toast.LENGTH_LONG
                    )
                    toast.show()
                    delay(3000)
                    finish()
                }else {
                    val params = getLoginParamUseCase.execute()
                    val profile = loginUseCase.execute(params)
                    setProfile(profile)
                    if (profile.accountType == AccountType.GENERAL) {
                        val intent = Intent(this@SplashScreenActivity, GeneralActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                    if (profile.accountType == AccountType.WORKER) {
                        val intent = Intent(this@SplashScreenActivity, WorkerActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }
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