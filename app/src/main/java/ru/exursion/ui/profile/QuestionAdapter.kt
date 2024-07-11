package ru.exursion.ui.profile

import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import com.livermor.delegateadapter.delegate.CompositeDelegateAdapter
import com.livermor.delegateadapter.delegate.ViewBindingDelegateAdapter
import ru.bibaboba.kit.ui.utils.DropDownItemUtil
import ru.exursion.R
import ru.exursion.data.models.Question
import ru.exursion.databinding.ItemFrequentlyQuestionBinding


class DropDownQuestionAdapter : ViewBindingDelegateAdapter<Question, ItemFrequentlyQuestionBinding>(
    ItemFrequentlyQuestionBinding::inflate
) {
    override fun isForViewType(item: Any): Boolean = item is Question

    override fun ItemFrequentlyQuestionBinding.onBind(item: Question) {
        questionTitle.text = item.question
        answerText.text = item.answer

        uncoverBtn.setOnClickListener{
            if(!answerText.isVisible) {
                uncoverBtn.setImageDrawable(AppCompatResources.getDrawable(root.context, R.drawable.ic_cross_rounded))
                DropDownItemUtil.expand(answerText)
            } else {
                uncoverBtn.setImageDrawable(AppCompatResources.getDrawable(root.context, R.drawable.ic_plus))
                DropDownItemUtil.collapse(answerText)
            }
        }
    }

    override fun Question.getItemId(): Any = question.hashCode()

}

class FaqCompositeAdapter: CompositeDelegateAdapter(DropDownQuestionAdapter())