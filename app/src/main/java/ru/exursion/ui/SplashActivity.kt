package ru.exursion.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import ru.exursion.App
import ru.exursion.R
import ru.exursion.data.InvalidToken
import ru.exursion.data.ProfileNotVerified
import ru.exursion.data.network.AuthHeaderInterceptor
import ru.exursion.databinding.ActivitySplashBinding
import ru.exursion.domain.ProfileUseCase
import ru.exursion.domain.settings.UserSettings
import ru.exursion.ui.auth.AuthActivity
import ru.exursion.ui.shared.ext.networkErrorDialog
import ru.exursion.ui.shared.ext.startAnimatedVectorDrawable
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    @Inject lateinit var userSettings: UserSettings
    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory

    private val viewModel: SplashViewModel by viewModels { viewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {

        (applicationContext as App).appComponent?.inject(this)

        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        AuthHeaderInterceptor.setSessionToken(userSettings.token ?: "")

        viewModel.responseCode.observe(this) { code ->
            if (code == null) {
                networkErrorDialog {
                    onDismiss { finish() }
                    onNeutralClick { finish() }
                }
                return@observe
            }

            val intent = if (userSettings.token == null || code != 200) {
                Intent(this, AuthActivity::class.java).apply {
                    if (code == 403) {
                        putExtra(AuthActivity.FRAGMENT_TO_NAVIGATE, R.id.enter_code_fragment)
                    }
                }
            } else {
                Intent(this, MainActivity::class.java)
            }

            startActivity(intent)
            finish()
        }

        binding.animationView.startAnimatedVectorDrawable(R.drawable.animation_splash) {
            viewModel.getProfile()
        }
    }

}

class SplashViewModel @Inject constructor(
    private val profileUseCase: ProfileUseCase
) : ViewModel() {

    private val compositeDisposable = CompositeDisposable()

    private fun invokeDisposable(block: () -> Disposable) = compositeDisposable.add(block())

    private val _responseCode = MutableLiveData<Int?>()
    val responseCode: LiveData<Int?> = _responseCode

    fun getProfile() = invokeDisposable {
        profileUseCase.getProfile()
            .subscribe({
                _responseCode.postValue(200)
            }, {
                _responseCode.postValue(when (it) {
                    is ProfileNotVerified -> 403
                    is InvalidToken -> 401
                    else -> null
                })
            })
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }

}