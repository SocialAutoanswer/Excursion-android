package ru.exursion.ui.routes.fragments

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import ru.exursion.R
import ru.exursion.databinding.FragmentDialogReviewBinding
import ru.exursion.ui.routes.RouteDetailsActivity
import ru.exursion.ui.routes.vm.RouteDetailsViewModel
import ru.exursion.ui.shared.ext.inject
import javax.inject.Inject

class ReviewDialogFragment: DialogFragment() {

    private var _binding: FragmentDialogReviewBinding? = null
    private val binding get() = _binding!!

    @Inject lateinit var viewModelFactory: ViewModelProvider.Factory
    private val viewModel by viewModels<RouteDetailsViewModel> { viewModelFactory }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        inject()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = FragmentDialogReviewBinding.inflate(inflater, container, false).also { _binding = it }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dialog?.window?.setBackgroundDrawableResource(R.color.transparent)

        binding.confirmButton.setOnClickListener {
            val activity = (activity as RouteDetailsActivity?) ?: return@setOnClickListener
            val routeId = activity.routeId ?: return@setOnClickListener

            viewModel.addReview(
                routeId,
                binding.rating.rating.toInt(),
                binding.reviewText.text.toString()
            )
        }

        viewModel.reviewPosted.observe(viewLifecycleOwner) {
            Toast.makeText(
                context,
                when(it){
                    true -> R.string.dialog_review_posted
                    false -> R.string.dialog_review_already_posted
                    null -> R.string.undefined_error
                },
                Toast.LENGTH_LONG
            ).show()

            dismiss()
        }

        binding.close.setOnClickListener { dismiss() }
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        _binding = null
    }

}