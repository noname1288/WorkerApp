package com.example.workerapp.data.error

sealed class AppError(
    override val message: String,
    override val cause: Throwable? = null
) : Throwable(message, cause) {

    // Network
    data class Network(
        val errorMessage: String,
        val httpCode: Int? = null,
        override val cause: Throwable? = null
    ) : AppError(errorMessage, cause)

    // Database
    data class Database(
        val errorMessage: String,
        override val cause: Throwable? = null
    ) : AppError(errorMessage, cause)

    // Business
    data class Business(
        val errorMessage: String
    ) : AppError(errorMessage)

    // Validation
    data class Validation(
        val field: String? = null,
        val errorMessage: String
    ) : AppError(errorMessage)

    // Unknown
    data class UnknownError(
        override val cause: Throwable? = null
    ) : AppError("Unknown error", cause)
}


// Extension để convert
fun Exception.toAppError(): AppError = AppError.UnknownError(
    cause = this
)