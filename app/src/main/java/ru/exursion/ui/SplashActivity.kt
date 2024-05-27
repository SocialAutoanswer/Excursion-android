package ru.exursion.ui

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.exursion.R
import ru.exursion.databinding.ActivitySplashBinding
import ru.exursion.ui.auth.AuthActivity
import ru.exursion.ui.shared.ext.startAnimatedVectorDrawable

@SuppressLint("CustomSplashScreen")
class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val binding = ActivitySplashBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.animationView.startAnimatedVectorDrawable(R.drawable.animation_splash) {
            if (false) { //TODO:check if user loggedIn
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, AuthActivity::class.java))
            }

            finish()
        }
    }

}