package ru.exursion.data

open class NetworkException : Exception()

class CanNotGetDataException : NetworkException()

class InternalServerError : NetworkException()

sealed class AuthException : NetworkException()
data object IncorrectPassword : AuthException()

data object IncorrectEmail : AuthException()