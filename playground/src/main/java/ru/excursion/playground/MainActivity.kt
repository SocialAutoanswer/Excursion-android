package ru.excursion.playground

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import ru.bibaboba.kit.states.Effect
import ru.bibaboba.kit.states.State
import ru.bibaboba.kit.states.StateMachine
import ru.excursion.playground.databinding.ActivityMainBinding

sealed class MainState: State {

    data object InitialState: MainState()
    data class FirstState(val s: String): MainState()
    data class SecondState(val s: String): MainState()
}

sealed class MainEffects: Effect {
    data object FirstEffect: MainEffects()
}

class MainActivity : AppCompatActivity() {


    private val binding: ActivityMainBinding by lazy { ActivityMainBinding.inflate(layoutInflater) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
    }

}