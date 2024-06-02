package ru.exursion.ui.routes.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import ru.exursion.databinding.FragmentRouteDetailsBinding

class RouteDetailsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return FragmentRouteDetailsBinding.inflate(inflater, container, false).root
    }
}