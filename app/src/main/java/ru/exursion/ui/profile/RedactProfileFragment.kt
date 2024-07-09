package ru.exursion.ui.profile

import android.view.View
import androidx.navigation.fragment.findNavController
import ru.bibaboba.kit.ui.BaseFragment
import ru.exursion.databinding.FragmentRedactProfileBinding

class RedactProfileFragment :
    BaseFragment<FragmentRedactProfileBinding>(FragmentRedactProfileBinding::class.java) {

    override fun setUpViews(view: View) = with(binding) {
        this.backButton.setOnClickListener{ findNavController().navigateUp() }
    }

}