package com.example.notificationservice.controller

import com.example.notificationservice.dto.NotificationResponse
import com.example.notificationservice.entity.NotificationChannel
import com.example.notificationservice.entity.NotificationStatus
import com.example.notificationservice.exception.GlobalExceptionHandler
import com.example.notificationservice.exception.NotificationNotFoundException
import com.example.notificationservice.service.NotificationService
import org.junit.jupiter.api.Test
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import java.time.Instant
import java.util.UUID

@WebMvcTest(NotificationController::class)
@Import(GlobalExceptionHandler::class)
class NotificationControllerTest {
    @Autowired lateinit var mockMvc: MockMvc
    @MockitoBean lateinit var service: NotificationService

    @Test
    fun shouldCreateNotification() {
        val id = UUID.randomUUID()
        whenever(service.create(org.mockito.kotlin.any())).thenReturn(response(id))
        mockMvc.perform(post("/notifications").contentType(MediaType.APPLICATION_JSON).content(
            """{"recipient":"test@gmail.com","message":"Test notification","channel":"EMAIL"}"""
        )).andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(id.toString()))
            .andExpect(jsonPath("$.recipient").value("test@gmail.com"))
            .andExpect(jsonPath("$.channel").value("EMAIL"))
            .andExpect(jsonPath("$.status").value("SENT"))
    }

    @Test
    fun shouldGetNotification() {
        val id = UUID.randomUUID()
        whenever(service.getById(eq(id))).thenReturn(response(id))
        mockMvc.perform(get("/notifications/{id}", id))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(id.toString()))
            .andExpect(jsonPath("$.recipient").value("test@gmail.com"))
            .andExpect(jsonPath("$.status").value("SENT"))
    }

    @Test
    fun shouldRejectInvalidRequest() {
        mockMvc.perform(post("/notifications").contentType(MediaType.APPLICATION_JSON).content(
            """{"recipient":"","message":"","channel":null}"""
        )).andExpect(status().isBadRequest)
    }

    @Test
    fun shouldReturn404WhenNotificationDoesNotExist() {
        val id = UUID.randomUUID()
        whenever(service.getById(eq(id))).thenThrow(NotificationNotFoundException(id))
        mockMvc.perform(get("/notifications/{id}", id))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.error").value("NOT_FOUND"))
            .andExpect(jsonPath("$.message").value("Notification not found: $id"))
    }

    private fun response(id: UUID) = NotificationResponse(
        id, "test@gmail.com", "Test notification", NotificationChannel.EMAIL,
        NotificationStatus.SENT, Instant.now(), Instant.now()
    )
}
