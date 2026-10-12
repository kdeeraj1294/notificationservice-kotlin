package com.example.notificationservice.provider

import com.example.notificationservice.entity.Notification
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component

@Component
class MockNotificationProvider : NotificationProvider {
    private val log = LoggerFactory.getLogger(javaClass)

    override fun send(notification: Notification) {
        log.info("Sending {} notification to {}", notification.channel, notification.recipient)
        if (notification.recipient.equals("fail@test.com", ignoreCase = true)) {
            throw IllegalStateException("Mock provider failed")
        }
    }
}
