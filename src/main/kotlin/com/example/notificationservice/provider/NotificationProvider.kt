package com.example.notificationservice.provider

import com.example.notificationservice.entity.Notification

interface NotificationProvider {
    fun send(notification: Notification)
}
