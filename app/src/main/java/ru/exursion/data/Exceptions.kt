package ru.exursion.data

open class NetworkException : Exception()

class CanNotGetDataException : NetworkException()

class InternalServerException : NetworkException()

class UnauthorizedException : NetworkException()

class ForbiddenException : NetworkException()

sealed class AuthException : NetworkException()

data object IncorrectPassword : AuthException()

data object IncorrectEmail : AuthException()

data object ProfileNotVerified : AuthException()

data object EmailAlreadyRegistered : AuthException()

data object IncorrectCode: AuthException()

data object InvalidToken: AuthException()