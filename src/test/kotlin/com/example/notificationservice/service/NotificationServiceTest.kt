package com.example.notificationservice.service

import com.example.notificationservice.dto.NotificationRequest
import com.example.notificationservice.entity.Notification
import com.example.notificationservice.entity.NotificationChannel
import com.example.notificationservice.entity.NotificationStatus
import com.example.notificationservice.exception.NotificationNotFoundException
import com.example.notificationservice.provider.NotificationProvider
import com.example.notificationservice.repository.NotificationRepository
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.mockito.kotlin.any
import org.mockito.kotlin.doThrow
import org.mockito.kotlin.eq
import org.mockito.kotlin.whenever
import org.mockito.kotlin.verify
import org.mockito.kotlin.times
import java.util.Optional
import java.util.UUID

@ExtendWith(MockitoExtension::class)
class NotificationServiceTest {
    @Mock lateinit var repository: NotificationRepository
    @Mock lateinit var provider: NotificationProvider
    @InjectMocks lateinit var service: NotificationService
    private lateinit var notification: Notification

    @BeforeEach
    fun setUp() {
        notification = Notification("test@gmail.com", "Test notification", NotificationChannel.EMAIL)
    }

    @Test
    fun shouldSendNotificationSuccessfully() {
        whenever(repository.save(any<Notification>())).thenAnswer { it.getArgument<Notification>(0) }
        service.create(NotificationRequest("test@gmail.com", "Test notification", NotificationChannel.EMAIL))
        verify(provider).send(any())
        verify(repository, org.mockito.kotlin.atLeast(2)).save(any())
    }

    @Test
    fun shouldMarkNotificationAsFailedWhenProviderFails() {
        whenever(repository.save(any<Notification>())).thenAnswer { it.getArgument<Notification>(0) }
        doThrow(RuntimeException("Provider unavailable")).whenever(provider).send(any())
        val response = service.create(NotificationRequest("test@gmail.com", "Test notification", NotificationChannel.EMAIL))
        assertEquals(NotificationStatus.FAILED, response.status)
        verify(provider).send(any())
    }

    @Test
    fun shouldReturnNotificationById() {
        val id = UUID.randomUUID()
        whenever(repository.findById(eq(id))).thenReturn(Optional.of(notification))
        val response = service.getById(id)
        assertNotNull(response)
        assertEquals("test@gmail.com", response.recipient)
        assertEquals(NotificationChannel.EMAIL, response.channel)
        verify(repository).findById(id)
    }

    @Test
    fun shouldThrowExceptionWhenNotificationDoesNotExist() {
        val id = UUID.randomUUID()
        whenever(repository.findById(eq(id))).thenReturn(Optional.empty())
        assertThrows(NotificationNotFoundException::class.java) { service.getById(id) }
        verify(repository).findById(id)
    }
}
