package ru.exursion.routes.ui

import ru.exursion.R
import ru.exursion.databinding.FragmentTownRouteTypesBinding
import ru.exursion.shared.ui.BackButtonFragment

class TownRouteTypesFragment : BackButtonFragment<FragmentTownRouteTypesBinding>(FragmentTownRouteTypesBinding::class.java) {

    override fun getTitleId(): Int = R.string.screen_town_routes_title



}