package ru.exursion.ui.profile

import android.view.View
import androidx.navigation.fragment.findNavController
import ru.bibaboba.kit.ui.BaseFragment
import ru.exursion.R
import ru.exursion.data.models.Question
import ru.exursion.databinding.FragmentQuestionsBinding

class QuestionsFragment: BaseFragment<FragmentQuestionsBinding>(FragmentQuestionsBinding::class.java) {

    override fun setUpViews(view: View) = with(binding) {
        val adapter = FaqCompositeAdapter()
        val questions = listOf(Question("Как дела?", "Заебись"), Question("Как дела?", "Хуёво"))

        adapter.swapData(questions)

        header.title.text = getString(R.string.screen_profile_faq)
        header.backButton.setOnClickListener{ findNavController().navigateUp() }
        questionRecycler.adapter = adapter
    }

}