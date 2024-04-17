package ru.exursion.shared.ui

import android.os.Bundle
import android.view.View
import androidx.annotation.CallSuper
import androidx.annotation.StringRes
import androidx.viewbinding.ViewBinding
import ru.bibaboba.kit.ui.BaseFragment
import ru.bibaboba.kit.ui.utils.inflateBinding
import ru.exursion.databinding.FragmentBackButtonBinding

abstract class BackButtonFragment<VB: ViewBinding>(
    private val childBindingClass: Class<VB>
) : BaseFragment<FragmentBackButtonBinding>(FragmentBackButtonBinding::class.java) {

    private var _childBinding: VB? = null
    protected val childBinding get() = _childBinding!!

    @StringRes
    protected abstract fun getTitleId(): Int

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        _childBinding = childBindingClass.inflateBinding(layoutInflater, binding.container)

        binding.backButton.setOnClickListener { activity?.onBackPressed() }
        binding.title.setText(getTitleId())
        binding.container.addView(childBinding.root)

        super.onViewCreated(view, savedInstanceState)
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        _childBinding = null
    }
}