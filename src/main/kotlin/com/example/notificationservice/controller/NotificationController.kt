package com.example.notificationservice.controller

import com.example.notificationservice.dto.NotificationRequest
import com.example.notificationservice.dto.NotificationResponse
import com.example.notificationservice.service.NotificationService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import java.util.UUID

@RestController
@RequestMapping("/notifications")
class NotificationController(private val service: NotificationService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun create(@Valid @RequestBody request: NotificationRequest): NotificationResponse = service.create(request)

    @GetMapping("/{id}")
    fun getById(@PathVariable id: UUID): NotificationResponse = service.getById(id)
}
