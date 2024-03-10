package ru.bibaboba.kit

import android.util.Log
import kotlin.reflect.KClass

object Logger {

    private var isInitiated = false
    private var isDebug = false

    fun init(isDebug: Boolean) {
        this.isDebug = isDebug

        isInitiated = true
    }

    fun <T: Any> debug(where: KClass<T>, message: String) = debugIfCan { print(where, message, Log::d) }

    fun <T: Any, K, V> debug(where: KClass<T>, title: String, list: Map<K, V>) = debugIfCan { printList(where, title, list, Log::d) }

    fun <T: Any, D> debug(where: KClass<T>, title: String, list: List<D>) = debugIfCan { printList(where, title, list, Log::d) }

    fun <T: Any> error(where: KClass<T>, e: Throwable) = print(where, "", e, Log::e)

    private fun debugIfCan(lambda: () -> Unit) {
        if (isDebug) lambda()
    }

    private fun <T: Any> print(where: KClass<T>, message: String, method: (String?, String) -> Unit) {
        method(where.simpleName, message)
    }

    private fun <T: Any, D> printList(where: KClass<T>, title: String, list: List<D>, method: (String?, String) -> Unit) {
        method(where.simpleName, "======= $title =======")
        list.forEachIndexed { i, d  -> method(where.simpleName, "$i -> $d") }
    }

    private fun <T: Any, K, V> printList(where: KClass<T>, title: String, list: Map<K, V>, method: (String?, String) -> Unit) {
        method(where.simpleName, "======= $title =======")
        list.forEach { (key, value) -> method(where.simpleName, "$key -> $value") }
    }

    private fun <T: Any> print(
        where: KClass<T>,
        message: String,
        throwable: Throwable,
        method: (String?, String, Throwable) -> Unit
    ) {
        method(where.simpleName, message, throwable)
    }
}