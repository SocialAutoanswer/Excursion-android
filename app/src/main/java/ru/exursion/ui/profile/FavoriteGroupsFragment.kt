package ru.exursion.ui.profile

import android.view.View
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import ru.bibaboba.kit.ui.BaseFragment
import ru.exursion.R
import ru.exursion.databinding.FragmentFavoriteGroupsBinding
import ru.exursion.ui.shared.content.BaseContentFragment

class FavoriteGroupsFragment: BaseFragment<FragmentFavoriteGroupsBinding>(FragmentFavoriteGroupsBinding::class.java) {

    override fun setUpViews(view: View) = with(binding) {
        val favoriteBundle = bundleOf(
            BaseContentFragment.IS_FAVORITE_BUNDLE_KEY to true
        )

        routesButton.setOnClickListener{
            findNavController().navigate(R.id.routesFragment, favoriteBundle)
        }

        hotelsButton.setOnClickListener{
            findNavController().navigate(R.id.hotelsFragment, favoriteBundle)
        }

        eventsButton.setOnClickListener{
            findNavController().navigate(R.id.eventsFragment, favoriteBundle)
        }

        header.title.text = getString(R.string.screen_favorites_title)
        header.backButton.setOnClickListener{ findNavController().navigateUp() }
    }

}