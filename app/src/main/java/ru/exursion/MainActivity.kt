package ru.exursion

import android.animation.ObjectAnimator
import android.annotation.SuppressLint
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import android.view.animation.AnticipateInterpolator
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.animation.doOnEnd
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import ru.exursion.databinding.ActivityMainBinding
import java.time.Instant

class MainActivity : AppCompatActivity() {

    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        configureSplashScreen()
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        with(binding) {
            //mainBottomNavigation.itemIconTintList = null
            //mainBottomNavigation.setupWithNavController(findNavController(R.id.nav_container))

        }
    }

    @SuppressLint("NewApi")
    private fun configureSplashScreen() {

        splashScreen.setSplashScreenTheme(R.style.Theme_App_Starting)

        splashScreen.setOnExitAnimationListener { splashScreenView ->
            ObjectAnimator.ofFloat(
                splashScreenView,
                View.TRANSLATION_Y,
                0f,
                splashScreenView.height.toFloat()
            ).apply {
                interpolator = AnticipateInterpolator()
                duration = 300L
                doOnEnd { splashScreenView.remove() }
                start()
            }
        }

        splashScreen.clearOnExitAnimationListener()

    }
}