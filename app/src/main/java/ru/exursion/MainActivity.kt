package ru.exursion

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.exursion.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_container) as NavHostFragment
        navController = navHostFragment.navController

        with(binding) {
            mainBottomNavigation.itemIconTintList = null
            navController?.let { mainBottomNavigation.setupWithNavController(it) }
        }
    }

    @Deprecated("using onBackPressed")
    override fun onBackPressed() {
        if (navController?.popBackStack() == false) super.onBackPressed()
    }
}