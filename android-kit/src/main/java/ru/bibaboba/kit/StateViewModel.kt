package ru.bibaboba.kit

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import ru.bibaboba.kit.states.Effect
import ru.bibaboba.kit.states.State

abstract class StateViewModel<S: State, E: Effect> : ViewModel() {

    protected val _state = MutableLiveData<S>()
    val state: LiveData<S> = _state

    protected val _effect = SingleLiveEvent<E>()
    val effect: LiveData<E> = _effect
}