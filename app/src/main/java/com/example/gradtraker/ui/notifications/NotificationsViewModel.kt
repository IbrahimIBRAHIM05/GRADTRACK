package com.example.gradtraker.ui.notifications

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

data class NotificationEntry(
    val id: String,
    val title: String,
    val message: String,
    val timestamp: String,
    val category: String,
    val isRead: Boolean = false,
    val taskId: String? = null
)

class NotificationsViewModel : ViewModel() {
    private val _notifications = MutableStateFlow<List<NotificationEntry>>(emptyList())
    val notifications: StateFlow<List<NotificationEntry>> = _notifications.asStateFlow()

    fun setNotifications(notifications: List<NotificationEntry>) {
        _notifications.value = notifications
    }

    fun markRead(id: String) {
        _notifications.value = _notifications.value.map { item ->
            if (item.id == id) item.copy(isRead = true) else item
        }
    }

    fun markAllRead() {
        _notifications.value = _notifications.value.map { it.copy(isRead = true) }
    }
}
