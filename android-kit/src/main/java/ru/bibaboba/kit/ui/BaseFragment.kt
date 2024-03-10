package ru.bibaboba.kit.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.CallSuper
import androidx.fragment.app.Fragment
import androidx.viewbinding.ViewBinding
import ru.bibaboba.kit.ui.utils.inflateBinding

abstract class BaseFragment<VB: ViewBinding> (private val bindingClass: Class<VB>) : Fragment() {

    private var _binding: VB? = null
    /** Should be called between [onCreateView] and [onDestroyView] */
    protected val binding get() = _binding!!

    @CallSuper
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = bindingClass.inflateBinding(inflater, container).also { _binding = it }?.root ?: throw IllegalStateException("Failed to instantiate binding")

    @CallSuper
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        getStartData()
        setUpViews()
        setUpObservers()
    }

    /** Get data from navigation bundle or something like this */
    protected open fun getStartData() { }

    /** Here you can set up text, click listeners etc */
    protected open fun setUpViews() { }

    /** Here you can set up observers for [LiveData] or anything other observable */
    protected open fun setUpObservers() { }

    @CallSuper
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}