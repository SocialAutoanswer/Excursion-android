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

    private val stateMachine = StateMachine.Builder()
        .lifecycleOwner(this)
        .addState(MainState.InitialState::class, ::onInitialState)
        .addState(MainState.FirstState::class, ::onFirstState)
        .addState(MainState.SecondState::class, ::onSecondState)
        .addEffect(MainEffects.FirstEffect::class) { Toast.makeText(this, "effect!", Toast.LENGTH_SHORT).show() }
        .initialState(MainState.InitialState)
        .build()

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        binding.firstButton.setOnClickListener {
            stateMachine.submit(MainState.FirstState("kek"))
        }
        binding.secondButton.setOnClickListener {
            stateMachine.submit(MainState.SecondState("olololololol"))
        }
        binding.prevButton.setOnClickListener {
            stateMachine.backToPreviousState()
        }
        binding.effectButton.setOnClickListener { stateMachine.submit(MainEffects.FirstEffect) }
    }

    private fun onInitialState(state: MainState.InitialState) {
        binding.mainText.text = "initial state"
    }

    private fun onFirstState(state: MainState.FirstState) {
        binding.mainText.text = state.s
    }

    private fun onSecondState(state: MainState.SecondState) {
        binding.mainText.text = "${state.s} it's second!"
    }
}