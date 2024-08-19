package ru.exursion.ui.profile

import ru.exursion.R
import ru.exursion.ui.shared.content.BaseContentFragment

class QuestionsFragment: BaseContentFragment() {

    override val adapter = DropDownQuestionAdapter()

    override val titleResId: Int
        get() = R.string.screen_profile_faq

    override fun getData() {
        viewModel.getQuestions()
    }

}