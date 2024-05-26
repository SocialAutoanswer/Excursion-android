package ru.bibaboba.kit.util

interface Mapper<I, O> {

    fun map(input: I): O

    fun reverseMap(input: O): I {
        throw NotImplementedError("Reverse map method is not implemented")
    }

    fun mapList(input: List<I>): List<O> = input.map { map(it) }

    fun reverseMapList(input: List<O>): List<I> = input.map { reverseMap(it) }
}