package ru.exursion.ui.auth

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import com.jakewharton.threetenabp.AndroidThreeTen
import ru.exursion.R

class AuthActivity : AppCompatActivity() {

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_auth)

        AndroidThreeTen.init(application)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.auth_nav_container) as NavHostFragment
        navController = navHostFragment.navController
    }

    @Deprecated("using onBackPressed")
    override fun onBackPressed() {
        if (navController?.popBackStack() == false) super.onBackPressed()
    }
}