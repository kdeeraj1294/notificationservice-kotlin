package com.example.notificationservice.dto

import com.example.notificationservice.entity.NotificationChannel
import com.example.notificationservice.entity.NotificationStatus
import java.time.Instant
import java.util.UUID

data class NotificationResponse(
    val id: UUID?,
    val recipient: String,
    val message: String,
    val channel: NotificationChannel,
    val status: NotificationStatus,
    val createdAt: Instant,
    val updatedAt: Instant?
)
