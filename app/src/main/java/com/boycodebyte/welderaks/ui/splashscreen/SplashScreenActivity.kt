package com.boycodebyte.welderaks.ui.splashscreen

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.boycodebyte.welderaks.MainActivity
import com.boycodebyte.welderaks.data.exceptions.LoginParamException
import com.boycodebyte.welderaks.domain.models.ErrorResult
import com.boycodebyte.welderaks.domain.models.PendingResult
import com.boycodebyte.welderaks.domain.models.SuccessResult
import com.boycodebyte.welderaks.data.repositories.LoginParamRepository
import com.boycodebyte.welderaks.data.repositories.LoginUsersRepository
import com.boycodebyte.welderaks.data.storage.FirebaseStorage
import com.boycodebyte.welderaks.data.storage.PrefStorage
import com.boycodebyte.welderaks.domain.usecase.GetLoginParamUseCase
import com.boycodebyte.welderaks.domain.usecase.LoginUseCase
import com.boycodebyte.welderaks.ui.login.LoginActivity
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.delay

class SplashScreenActivity : AppCompatActivity() {

    private var job: Job? = null
    private val getLoginParamRepository =
        GetLoginParamUseCase(LoginParamRepository((PrefStorage(this))))
    private val loginUseCase = LoginUseCase(LoginUsersRepository(FirebaseStorage()))
    override fun onStart() {
        super.onStart()
        job = lifecycleScope.async {
            delay(2000)
            try {
                val params = getLoginParamRepository.execute()
                loginUseCase.execute(params) {
                    when (it) {
                        is SuccessResult -> {
                            val intent = Intent(this@SplashScreenActivity, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        is ErrorResult -> {
                            val intent =
                                Intent(this@SplashScreenActivity, LoginActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                        is PendingResult -> {
                        }
                    }
                }
            } catch (e: LoginParamException) {
                val intent = Intent(this@SplashScreenActivity, LoginActivity::class.java)
                startActivity(intent)
            }
        }
    }

    override fun onStop() {
        super.onStop()
        job?.cancel()
    }
}