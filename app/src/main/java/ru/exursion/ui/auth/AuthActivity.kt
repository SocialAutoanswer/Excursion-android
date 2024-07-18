package ru.exursion.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.jakewharton.threetenabp.AndroidThreeTen
import ru.exursion.R

class AuthActivity : AppCompatActivity() {

    companion object {
        const val FRAGMENT_TO_NAVIGATE = "fragment_to_navigate"
        const val IS_START_SCREEN = "is_start_screen"
    }

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        AndroidThreeTen.init(application)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.auth_nav_container) as NavHostFragment
        navController = navHostFragment.navController

        intent.extras?.getInt(FRAGMENT_TO_NAVIGATE)?.let {
            navController?.navigate(it, bundleOf(IS_START_SCREEN to true))
        }
    }

    @Deprecated("using onBackPressed")
    override fun onBackPressed() {
        if (navController?.popBackStack() == false) super.onBackPressed()
    }
}