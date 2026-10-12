package com.example.notificationservice.exception

import java.util.UUID

class NotificationNotFoundException(id: UUID) : RuntimeException("Notification not found: $id")
