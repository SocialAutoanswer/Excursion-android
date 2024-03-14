package ru.bibaboba.kit

import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.disposables.Disposable
import ru.bibaboba.kit.states.Effect
import ru.bibaboba.kit.states.State

class RxStateViewModel<S: State, E: Effect> : StateViewModel<S, E>() {

    protected val compositeDisposable = CompositeDisposable()

    protected fun invokeDisposable(block: () -> Disposable) = compositeDisposable.add(block())

    override fun onCleared() {
        compositeDisposable.dispose()
    }
}