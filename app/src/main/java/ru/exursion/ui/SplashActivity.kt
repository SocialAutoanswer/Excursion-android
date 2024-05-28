package ru.exursion.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.exursion.App
import ru.exursion.R
import ru.exursion.databinding.ActivitySplashBinding
import ru.exursion.domain.settings.UserSettings
import ru.exursion.ui.auth.AuthActivity
import ru.exursion.ui.shared.ext.startAnimatedVectorDrawable
import javax.inject.Inject

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    @Inject lateinit var userSettings: UserSettings

    override fun onCreate(savedInstanceState: Bundle?) {

        (applicationContext as App).appComponent?.inject(this)

        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.animationView.startAnimatedVectorDrawable(R.drawable.animation_splash) {
            startActivity(Intent(this, if (userSettings.token == null) AuthActivity::class.java else MainActivity::class.java))
            finish()
        }
    }

}