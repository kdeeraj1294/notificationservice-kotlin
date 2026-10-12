package com.example.notificationservice.exception

import com.example.notificationservice.dto.ApiError
import org.springframework.http.HttpStatus
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestControllerAdvice
import java.time.Instant

@RestControllerAdvice
class GlobalExceptionHandler {
    @ExceptionHandler(NotificationNotFoundException::class)
    @ResponseStatus(HttpStatus.NOT_FOUND)
    fun handleNotFound(ex: NotificationNotFoundException) =
        ApiError(Instant.now(), 404, "NOT_FOUND", ex.message ?: "Notification not found")

    @ExceptionHandler(MethodArgumentNotValidException::class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    fun handleValidation(ex: MethodArgumentNotValidException): ApiError {
        val message = ex.bindingResult.fieldErrors.firstOrNull()
            ?.let { "${it.field}: ${it.defaultMessage}" } ?: "Invalid request"
        return ApiError(Instant.now(), 400, "BAD_REQUEST", message)
    }
}
