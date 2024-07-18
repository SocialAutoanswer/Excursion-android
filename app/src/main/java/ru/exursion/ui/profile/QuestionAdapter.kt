package ru.exursion.ui.profile

import android.view.ViewGroup
import androidx.appcompat.content.res.AppCompatResources
import androidx.core.view.isVisible
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import ru.bibaboba.kit.ui.ItemViewHolder
import ru.bibaboba.kit.ui.utils.DropDownItemUtil
import ru.exursion.R
import ru.exursion.data.models.Question
import ru.exursion.databinding.ItemFrequentlyQuestionBinding


class DropDownQuestionAdapter :
    PagingDataAdapter<Question, ItemViewHolder<ItemFrequentlyQuestionBinding, Question>>(
        QuestionsDiffUtilCallback
    ) {

    override fun onBindViewHolder(
        holder: ItemViewHolder<ItemFrequentlyQuestionBinding, Question>,
        position: Int
    ) {
        getItem(position)?.let { holder.bind(it) }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder<ItemFrequentlyQuestionBinding, Question> {
        return ItemViewHolder.create(parent) { binding, question ->
            with(binding) {
                questionTitle.text = question.question
                answerText.text = question.answer

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
        }
    }

}

private object QuestionsDiffUtilCallback : DiffUtil.ItemCallback<Question>() {
    override fun areItemsTheSame(oldItem: Question, newItem: Question): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Question, newItem: Question): Boolean {
        return oldItem == newItem
    }
}