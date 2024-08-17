package ru.exursion.ui.routes

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import ru.exursion.R
import ru.exursion.databinding.ActivityRouteDetailsBinding
import ru.exursion.ui.routes.fragments.ReviewDialogFragment
import ru.exursion.ui.routes.fragments.RouteAudiosDialog

class RouteDetailsActivity : AppCompatActivity(), RouteActivityController {

    companion object {
        private const val ROUTE_ID = "route_id"

        fun start(activity: Activity?, routeId: Long, tagId: Long) {
            val activity = activity ?: return
            Intent(activity, RouteDetailsActivity::class.java).apply {
                putExtra(ROUTE_ID, routeId)
                activity.startActivity(this)
            }
        }
    }

    enum class ButtonState{
        IDLE,
        ROUTE_IS_PAID,
        ROUTE_IS_NOT_PAID,
        REVIEW_IS_GIVEN,
        REVIEW_IS_NOT_GIVEN
    }

    private var _binding: ActivityRouteDetailsBinding? = null
    private val binding get() = _binding!!


    var routeId: Long? = null
        private set
    var tagId: Long? = null
        private set

    private var navController: NavController? = null
    private var onPayButtonClick: (() -> Unit)? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRouteDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        routeId = intent.getLongExtra(ROUTE_ID, -1)

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_container) as NavHostFragment
        navController = navHostFragment.navController

        binding.routeButton.setOnClickListener {
            navHostFragment.navController.navigate(R.id.routeMapFragment)
        }

        binding.buy.setOnClickListener {
            onPayButtonClick?.invoke()
        }
    }

    @Deprecated("using onBackPressed")
    override fun onBackPressed() {
        if (navController?.popBackStack() == false) super.onBackPressed()
    }

    @SuppressLint("RestrictedApi")
    override fun setButtonUiState(state: ButtonState) {
        if(state != ButtonState.IDLE) {
            setButtonUiState(ButtonState.IDLE)
        }

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_container) as NavHostFragment

        when(state) {
            ButtonState.IDLE -> {
                binding.buy.isVisible = false
                binding.routeButton.isVisible = false
                binding.confirmButton.isVisible = false
                binding.confirmButton.text = ""
                binding.confirmButton.isEnabled = false
            }
            ButtonState.ROUTE_IS_NOT_PAID -> {
                binding.buy.isVisible = true
                binding.routeButton.isVisible = true
            }
            ButtonState.REVIEW_IS_GIVEN -> {
                binding.confirmButton.isVisible = true
                binding.confirmButton.text = getText(R.string.screen_route_details_give_review)
                binding.confirmButton.isEnabled = false
            }
            ButtonState.REVIEW_IS_NOT_GIVEN -> {
                binding.confirmButton.isVisible = true
                binding.confirmButton.text = getText(R.string.screen_route_details_give_review)
                binding.confirmButton.isEnabled = true

                binding.confirmButton.setOnClickListener {
                    val lastDestination = navHostFragment.navController.currentBackStack.value.last().destination

                    if (lastDestination.id == R.id.reviewsFragment) {
                        ReviewDialogFragment().show(supportFragmentManager, "tag")
                    }
                }
            }
            ButtonState.ROUTE_IS_PAID -> {
                binding.confirmButton.isVisible = true
                binding.confirmButton.isEnabled = true
                binding.confirmButton.text = getString(R.string.screen_route_details_start_route)
                binding.confirmButton.setOnClickListener {
                    val lastDestination = navHostFragment.navController.currentBackStack.value.last().destination

                    if (lastDestination.id == R.id.routeMapFragment) {
                        RouteAudiosDialog().show(supportFragmentManager, "tag")
                    }
                    if (lastDestination.id == R.id.fragment_route_details) {
                        navHostFragment.navController.navigate(R.id.routeMapFragment)
                    }
                }
            }
        }
    }

    override fun setOnPayButtonClick(callback: () -> Unit) {
        onPayButtonClick = callback
    }
}