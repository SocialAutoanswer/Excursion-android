package ru.bibaboba.kit.ui

import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.viewbinding.ViewBinding
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import ru.bibaboba.kit.ui.utils.inflateBinding

open class BaseBottomSheetDialogFragment<VB: ViewBinding> (private val bindingClass: Class<VB>) : BottomSheetDialogFragment() {

    private var _binding: VB? = null
    /** Should be called between [onCreateView] and [onDestroyView] */
    protected val binding get() = _binding!!

    private var onDismissCallback: (() -> Unit)? = null

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = bindingClass.inflateBinding(inflater, container).also { _binding = it }?.root ?: throw IllegalStateException("Failed to instantiate binding")

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getStartData()
        setUpViews(view)
        setUpObservers()
    }

    /** Get data from navigation bundle or something like this */
    protected open fun getStartData() { }

    /** Here you can set up text, click listeners etc */
    protected open fun setUpViews(view: View) { }

    /** Here you can set up observers for [LiveData] or anything other observable */
    protected open fun setUpObservers() { }

    fun setOnDismiss(callback: () -> Unit) {
        onDismissCallback = callback
    }

    @CallSuper
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        onDismissCallback?.invoke()
    }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}