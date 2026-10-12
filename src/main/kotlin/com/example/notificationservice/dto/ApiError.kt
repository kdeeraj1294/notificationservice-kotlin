package com.example.notificationservice.dto

import java.time.Instant

data class ApiError(
    val timestamp: Instant,
    val status: Int,
    val error: String,
    val message: String
)
