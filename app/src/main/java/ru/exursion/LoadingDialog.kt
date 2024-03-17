package ru.exursion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import ru.exursion.databinding.LoadingScreenBinding

class LoadingDialog: DialogFragment() {

    //recommendation for DialogFragment
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.custom_dialog)
    }

    //recommendation for DialogFragment
    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            WindowManager.LayoutParams.MATCH_PARENT,
            WindowManager.LayoutParams.MATCH_PARENT
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ) = LoadingScreenBinding.inflate(layoutInflater).root
}