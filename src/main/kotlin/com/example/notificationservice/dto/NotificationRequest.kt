package com.example.notificationservice.dto

import com.example.notificationservice.entity.NotificationChannel
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class NotificationRequest(
    @field:NotBlank(message = "Recipient is required")
    val recipient: String,

    @field:NotBlank(message = "Message is required")
    val message: String,

    @field:NotNull(message = "Channel is required")
    val channel: NotificationChannel?
)
