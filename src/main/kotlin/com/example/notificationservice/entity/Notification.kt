package com.example.notificationservice.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant
import java.util.UUID

@Entity
@Table(name = "notifications")
class Notification protected constructor() {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    var id: UUID? = null
        protected set

    @Column(nullable = false)
    var recipient: String = ""
        protected set

    @Column(nullable = false, length = 2000)
    var message: String = ""
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var channel: NotificationChannel = NotificationChannel.EMAIL
        protected set

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    var status: NotificationStatus = NotificationStatus.PENDING
        protected set

    @Column(nullable = false, updatable = false)
    var createdAt: Instant = Instant.now()
        protected set

    var updatedAt: Instant? = null
        protected set

    constructor(recipient: String, message: String, channel: NotificationChannel) : this() {
        this.recipient = recipient
        this.message = message
        this.channel = channel
        this.status = NotificationStatus.PENDING
        this.createdAt = Instant.now()
    }

    fun markProcessing() {
        status = NotificationStatus.PROCESSING
        updatedAt = Instant.now()
    }

    fun markSent() {
        status = NotificationStatus.SENT
        updatedAt = Instant.now()
    }

    fun markFailed() {
        status = NotificationStatus.FAILED
        updatedAt = Instant.now()
    }
}
