package ru.bibaboba.kit.docs

import ru.bibaboba.kit.states.*


/**
 * State machine is instrument for manage states of ui entity (Fragment or Activity).
 * It can be created only with [StateMachine.Builder]. When you created [StateMachine] you should
 * specify all states and effects, that will be manage by this.
 *
 * Speaking of states and effects:
 *
 * [State] - data set or lack of data related to some view, that should shown to user for long time.
 * For example: loading screen or screen with list of data
 *
 * [Effect] - one time shit that should be shown to user and forgotten. For example: notification, Toast
 *
 * Every state or effect added to [StateMachine] must be inherit from
 *
 * Example of building [StateMachine]:
 * ```
 * private val stateMachine = StateMachine.Builder()
 *         .lifecycleOwner(this)
 *         .addState(InitialState::class, ::onInitialState)
 *         .addState(FirstState::class, ::onFirstState)
 *         .addState(SecondState::class, ::onSecondState)
 *         .addEffect(FirstEffect::class) { Toast.makeText(this, "effect!", Toast.LENGTH_SHORT).show() }
 *         .initialState(InitialState)
 *         .build()
 * ```
 *
 * Examples of use [StateMachine]:
 *
 * ```kotlin
 * binding.firstButton.setOnClickListener {
 *    stateMachine.submit(MainState.FirstState("kek"))
 * }
 * binding.secondButton.setOnClickListener {
 *    stateMachine.submit(MainState.SecondState("olololololol"))
 * }
 * binding.prevButton.setOnClickListener {
 *    stateMachine.backToPreviousState()
 * }
 * binding.effectButton.setOnClickListener { stateMachine.submit(MainEffects.FirstEffect) }
 * ```
 */
annotation class StateMachineDoc
