package com.example.gradtraker.ui.notifications

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp

private val PageBackground = Color(0xFFF8F4EA)
private val BrandGreen = Color(0xFF25463B)
private val SecondaryGreen = Color(0xFF4F7A64)
private val LightGreen = Color(0xFFE6EEDF)
private val NavyText = Color(0xFF27344C)

@Composable
fun NotificationsScreen(
    viewModel: NotificationsViewModel,
    onNotificationSelected: (NotificationEntry) -> Unit = {},
    modifier: Modifier = Modifier
) {
    val notifications by viewModel.notifications.collectAsState()
    val unreadCount = notifications.count { !it.isRead }

    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(PageBackground)
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Column(Modifier.weight(1f)) {
                Text("Recent activity", color = SecondaryGreen, style = MaterialTheme.typography.bodyMedium)
            }
            TextButton(onClick = viewModel::markAllRead, enabled = unreadCount > 0) {
                Text("Mark all as read", color = if (unreadCount > 0) BrandGreen else SecondaryGreen)
            }
        }

        if (notifications.isEmpty()) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth().padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(color = LightGreen, shape = RoundedCornerShape(10.dp), modifier = Modifier.size(42.dp)) {
                        androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) {
                            Text("◇", color = SecondaryGreen, style = MaterialTheme.typography.titleMedium)
                        }
                    }
                    Column(Modifier.padding(start = 12.dp)) {
                        Text("No notifications", color = BrandGreen, fontWeight = FontWeight.SemiBold)
                        Text("New updates will appear here when available.", color = SecondaryGreen, style = MaterialTheme.typography.bodySmall)
                    }
                }
            }
        } else {
            notifications.forEach { notification ->
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            viewModel.markRead(notification.id)
                            onNotificationSelected(notification)
                        },
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.White)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            color = if (notification.isRead) LightGreen else Color(0xFFEFEAFF),
                            shape = RoundedCornerShape(10.dp),
                            modifier = Modifier.size(40.dp)
                        ) {
                            androidx.compose.foundation.layout.Box(contentAlignment = Alignment.Center) {
                                Text("◇", color = SecondaryGreen, style = MaterialTheme.typography.titleMedium)
                            }
                        }
                        Column(Modifier.weight(1f).padding(start = 12.dp)) {
                            Text(notification.title, color = NavyText, fontWeight = FontWeight.SemiBold)
                            Spacer(Modifier.height(3.dp))
                            Text(notification.message, color = SecondaryGreen, style = MaterialTheme.typography.bodySmall)
                        }
                        Column(horizontalAlignment = Alignment.End, modifier = Modifier.padding(start = 8.dp)) {
                            Text(notification.timestamp, color = SecondaryGreen, style = MaterialTheme.typography.labelSmall)
                            Spacer(Modifier.height(6.dp))
                            Surface(color = LightGreen, shape = RoundedCornerShape(50)) {
                                Text(notification.category, color = SecondaryGreen, style = MaterialTheme.typography.labelSmall, modifier = Modifier.padding(horizontal = 9.dp, vertical = 4.dp))
                            }
                            if (!notification.isRead) {
                                Text("Unread", color = BrandGreen, style = MaterialTheme.typography.labelSmall)
                            }
                        }
                    }
                }
            }
        }
    }
}
