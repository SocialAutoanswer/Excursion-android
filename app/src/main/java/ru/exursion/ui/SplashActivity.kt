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

        viewModel.profileVerified.observe(this) { verified ->
            startActivity(Intent(this, if (userSettings.token == null || !verified) AuthActivity::class.java else MainActivity::class.java))
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

    private val _profileVerified = MutableLiveData<Boolean>()
    val profileVerified: LiveData<Boolean> = _profileVerified

    fun getProfile() = invokeDisposable {
        profileUseCase.getProfile()
            .subscribe({
                _profileVerified.postValue(true)
            }, {
                _profileVerified.postValue(when (it) {
                    is ProfileNotVerified -> false
                    is InvalidToken -> false
                    else -> true
                })
            })
    }

    override fun onCleared() {
        compositeDisposable.dispose()
    }

}