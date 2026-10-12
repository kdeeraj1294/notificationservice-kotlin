package com.example.notificationservice.repository

import com.example.notificationservice.entity.Notification
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface NotificationRepository : JpaRepository<Notification, UUID>
