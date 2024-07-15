package ru.exursion.ui.routes

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import ru.exursion.R
import ru.exursion.databinding.ActivityRouteDetailsBinding

class RouteDetailsActivity : AppCompatActivity() {

    companion object {
        val ROUTE_ID = "route_id"
    }

    private var navController: NavController? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val binding = ActivityRouteDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_container) as NavHostFragment
        navController = navHostFragment.navController
    }

    @Deprecated("using onBackPressed")
    override fun onBackPressed() {
        if (navController?.popBackStack() == false) super.onBackPressed()
    }
}