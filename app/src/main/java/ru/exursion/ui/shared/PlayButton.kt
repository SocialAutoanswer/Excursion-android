package ru.exursion.ui.shared

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.content.res.AppCompatResources
import androidx.appcompat.widget.AppCompatImageButton
import androidx.core.view.setPadding
import ru.exursion.R
import ru.exursion.domain.ext.toDpPixels

class PlayButton(context: Context, attrs: AttributeSet) : AppCompatImageButton(context, attrs) {

    init {
        background = AppCompatResources.getDrawable(context, R.drawable.background_play_button)
        setImageResource(R.drawable.ic_play)
        scaleType = ScaleType.CENTER_INSIDE
        setPadding(9.toDpPixels(context))
        isEnabled = false
        setUiState(false)
    }

    private var _playerClickListener : OnPlayerClickListener? = null

    private val playerClickListener = object : OnPlayerClickListener {
        override fun onPlayClick() {
            setUiState(true)
            _playerClickListener?.onPlayClick()
        }

        override fun onPauseClick() {
            setUiState(false)
            _playerClickListener?.onPauseClick()
        }

        override fun onSetPosition(position: Int) {}
    }


    fun setUiState(isPlaying: Boolean) {
        if(isPlaying){
            setImageResource(R.drawable.ic_pause)
            setOnClickListener { playerClickListener.onPauseClick() }
        } else {
            setImageResource(R.drawable.ic_play)
            setOnClickListener { playerClickListener.onPlayClick() }
        }
    }

    fun setOnPlayerClickListener(listener: OnPlayerClickListener) {
        _playerClickListener = listener
        isEnabled = true
    }

}