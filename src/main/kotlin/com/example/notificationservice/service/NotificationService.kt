package com.example.notificationservice.service

import com.example.notificationservice.dto.NotificationRequest
import com.example.notificationservice.dto.NotificationResponse
import com.example.notificationservice.entity.Notification
import com.example.notificationservice.exception.NotificationNotFoundException
import com.example.notificationservice.provider.NotificationProvider
import com.example.notificationservice.repository.NotificationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class NotificationService(
    private val repository: NotificationRepository,
    private val provider: NotificationProvider
) {
    @Transactional
    fun create(request: NotificationRequest): NotificationResponse {
        val channel = requireNotNull(request.channel) { "Channel is required" }
        val notification = repository.save(Notification(request.recipient, request.message, channel))

        return try {
            notification.markProcessing()
            repository.save(notification)
            provider.send(notification)
            notification.markSent()
            toResponse(notification)
        } catch (_: Exception) {
            notification.markFailed()
            repository.save(notification)
            toResponse(notification)
        }
    }

    @Transactional(readOnly = true)
    fun getById(id: UUID): NotificationResponse {
        val notification = repository.findById(id)
            .orElseThrow { NotificationNotFoundException(id) }
        return toResponse(notification)
    }

    private fun toResponse(notification: Notification) = NotificationResponse(
        id = notification.id,
        recipient = notification.recipient,
        message = notification.message,
        channel = notification.channel,
        status = notification.status,
        createdAt = notification.createdAt,
        updatedAt = notification.updatedAt
    )
}
