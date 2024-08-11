package ru.bibaboba.kit.states

import androidx.annotation.IntRange
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleOwner
import ru.bibaboba.kit.Logger
import ru.bibaboba.kit.addObserver
import java.lang.IllegalArgumentException
import kotlin.reflect.KClass

class StateMachine private constructor(
    private val states: Map<KClass<*>, (State) -> Unit>,
    private val stateExitCallbacks: Map<KClass<*>, (State) -> Unit>,
    private val effects: Map<KClass<*>, (Effect) -> Unit>,
    private val lifecycleOwner: LifecycleOwner?,
    private val initialState: State?,
    private val unlimitedStateHistory: Boolean,
    private val stateHistoryLimit: Int,
    private val loggingEnabled: Boolean = false
) {

    private var currentState: State? = initialState

    private val stateHistory = ArrayDeque<State>()

    init {
        lifecycleOwner?.lifecycle?.addObserver { event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> initialState?.let { submitHard(it) }
                else -> {}
            }
        }
    }

    /** Change [currentState] without callback invocation and adding new [state] to [stateHistory] */
    fun <S: State> changeStateSilently(state: S) {
        currentState = state
    }

    /** Invoke callback related to [state].
     * Callback will be not invoked if [state] is equals to previous state
     */
    fun <S: State> submit(state: S) {
        if (loggingEnabled) {
            Logger.debug(this::class, "Submitting state: ${state::class.simpleName}")
        }
        if (currentState == state) {
            if (loggingEnabled) {
                Logger.debug(this::class, "Decline state ${state::class.simpleName} cause current state the same")
            }
            return
        }
        if (loggingEnabled) {
            Logger.debug(this::class, "State ${state::class.simpleName} submitted")
        }
        currentState?.let { stateExitCallbacks[it::class]?.invoke(it) }
        submitHard(state)
    }

    fun <E: Effect> submit(effect: E) {
        effects[effect::class]?.invoke(effect)
    }

    /** Back to previous state on [stateHistory] and invoke callback related to previous state.
     *  Current state will be removed.
     *  If there is no previous state do nothing.
     */
    fun backToPreviousState() {
        if (stateHistory.size == 1) return

        stateHistory.removeLast()
        currentState = stateHistory.last()
        currentState?.let { submitHard(it, false) }
    }

    private fun <S: State> submitHard(state: S, addToStateHistory: Boolean = true) {
        if (addToStateHistory) {
            addToStateHistory(state)
        }
        currentState = state
        states[state::class]?.invoke(state)
    }

    private fun addToStateHistory(state: State) {
        stateHistory.addLast(state)
        if (!unlimitedStateHistory && stateHistory.size > stateHistoryLimit) {
            stateHistory.removeFirst()
        }
    }

    companion object {
        const val STATE_HISTORY_LIMIT = 10
    }

    class Builder {

        private val states = HashMap<KClass<*>, (State) -> Unit>()
        private val stateExitCallbacks = HashMap<KClass<*>, (State) -> Unit>()
        private val effects = HashMap<KClass<*>, (Effect) -> Unit>()

        private var stateHistoryLimit: Int? = null
        private var unlimitedStateHistory: Boolean = false

        private var lifecycleOwner: LifecycleOwner? = null
        private var initialState: State? = null

        private var debugEnabled = false

        @Suppress("Unchecked_cast")
        fun <S: State> addState(stateClass: KClass<S>, callback: (S) -> Unit): Builder {
            states[stateClass] = callback as (State) -> Unit
            return this
        }

        @Suppress("Unchecked_cast")
        fun <S: State> addState(stateClass: KClass<S>, callback: (S) -> Unit, onExit: (S) -> Unit): Builder {
            states[stateClass] = callback as (State) -> Unit
            stateExitCallbacks[stateClass] = onExit as (State) -> Unit
            return this
        }

        @Suppress("Unchecked_cast")
        fun <E: Effect> addEffect(effectClass: KClass<E>, callback: (E) -> Unit): Builder {
            effects[effectClass] = callback as (Effect) -> Unit
            return this
        }

        fun lifecycleOwner(owner: LifecycleOwner): Builder {
            lifecycleOwner = owner
            return this
        }

        /** Sets the initial state. Must be used with [lifecycleOwner] */
        fun initialState(state: State): Builder {
            initialState = state
            return this
        }

        /** Sets the limit for [stateHistory] (inclusive). Default limit is 10 */
        fun stateHistoryLimit(@IntRange(from = 1) limit: Int): Builder {
            stateHistoryLimit = limit
            return this
        }

        /** Make state history unlimited. If add this property with [stateHistoryLimit] then limit will be not used */
        fun unlimitedStateHistory(): Builder {
            unlimitedStateHistory = true
            return this
        }

        fun enableDebugLogs(): Builder {
            debugEnabled = true
            return this
        }

        fun build(): StateMachine {
            if (initialState != null && lifecycleOwner == null) {
                throw IllegalStateException("Can not use initial state without lifecycle owner")
            }
            if (initialState != null && states[initialState!!::class] == null) {
                throw IllegalArgumentException("State ${initialState!!::class.simpleName} can not be initial because it's not in list of states")
            }

            return StateMachine(
                states,
                stateExitCallbacks,
                effects,
                lifecycleOwner,
                initialState,
                unlimitedStateHistory,
                stateHistoryLimit ?: STATE_HISTORY_LIMIT,
                debugEnabled
            )
        }
    }
}