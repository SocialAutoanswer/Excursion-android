package ru.exursion

import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.view.WindowManager
import androidx.fragment.app.DialogFragment
import ru.exursion.databinding.LoadingScreenBinding

class LoadingDialog: DialogFragment() {

    //recommendation for DialogFragment https://developer.android.com/reference/android/app/DialogFragment#BasicDialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NORMAL, R.style.CustomDialog)
    }

    //recommendation for DialogFragment https://islamassem.medium.com/custom-dialog-using-dialog-fragments-de6a0874b6a4
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