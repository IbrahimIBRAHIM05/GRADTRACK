package com.example.gradtraker.navigation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.role
import androidx.compose.ui.semantics.semantics
import androidx.compose.foundation.clickable
import androidx.compose.runtime.getValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp

private const val ROUTE_SEPARATOR = "\u001F"

object RouteHistory {
    fun current(encodedRoutes: String): String = encodedRoutes.split(ROUTE_SEPARATOR).last()

    fun canGoBack(encodedRoutes: String): Boolean = encodedRoutes.contains(ROUTE_SEPARATOR)

    fun parent(encodedRoutes: String): String {
        val routes = encodedRoutes.split(ROUTE_SEPARATOR)
        return if (routes.size > 1) routes[routes.lastIndex - 1] else routes.last()
    }

    fun push(encodedRoutes: String, route: String): String {
        if (current(encodedRoutes) == route) return encodedRoutes
        return "$encodedRoutes$ROUTE_SEPARATOR$route"
    }

    fun selectTopLevel(route: String): String = route

    fun pop(encodedRoutes: String): String {
        val routes = encodedRoutes.split(ROUTE_SEPARATOR)
        return if (routes.size > 1) routes.dropLast(1).joinToString(ROUTE_SEPARATOR) else encodedRoutes
    }
}

data class GradTrackDestination(val title: String, val icon: String)

@Composable
fun GradTrackBottomNavigation(
    destinations: List<GradTrackDestination>,
    selectedTitle: String,
    onSelect: (GradTrackDestination) -> Unit
) {
    val forest = Color(0xFF25463B)
    val cream = Color(0xFFF8F4EA)
    val selectedSurface = Color(0xFFE6EEDF)

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 10.dp, vertical = 6.dp)
            .shadow(16.dp, RoundedCornerShape(26.dp)),
        color = cream.copy(alpha = 0.88f),
        shape = RoundedCornerShape(26.dp),
        border = BorderStroke(1.dp, Color.White.copy(alpha = 0.82f)),
        tonalElevation = 7.dp
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .windowInsetsPadding(WindowInsets.navigationBars)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.White.copy(alpha = 0.58f),
                            cream.copy(alpha = 0.86f),
                            Color(0xFFF0EBDD).copy(alpha = 0.94f)
                        )
                    )
                )
        ) {
            Box(
                Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(
                        Brush.horizontalGradient(
                            listOf(Color.Transparent, forest.copy(alpha = 0.24f), Color.Transparent)
                        )
                    )
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState())
                    .padding(horizontal = 6.dp, vertical = 5.dp),
                horizontalArrangement = Arrangement.spacedBy(3.dp)
            ) {
                destinations.forEach { destination ->
                    val selected = destination.title == selectedTitle
                    val iconColor by animateColorAsState(
                        targetValue = if (selected) forest else forest.copy(alpha = 0.72f),
                        animationSpec = tween(durationMillis = 180),
                        label = "navigationIconColor"
                    )
                    val labelColor by animateColorAsState(
                        targetValue = if (selected) forest else forest.copy(alpha = 0.72f),
                        animationSpec = tween(durationMillis = 180),
                        label = "navigationLabelColor"
                    )
                    val indicatorWidth by animateDpAsState(
                        targetValue = if (selected) 62.dp else 54.dp,
                        animationSpec = tween(durationMillis = 180),
                        label = "navigationSelectionWidth"
                    )
                    Column(
                        modifier = Modifier
                            .width(92.dp)
                            .clickable(role = Role.Tab) { onSelect(destination) }
                            .semantics {
                                role = Role.Tab
                                contentDescription = destination.title
                            },
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        Surface(
                            color = if (selected) selectedSurface.copy(alpha = 0.94f) else Color.Transparent,
                            shape = RoundedCornerShape(16.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(width = indicatorWidth, height = 34.dp)
                                    .padding(vertical = 3.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(destination.icon, color = iconColor, style = MaterialTheme.typography.titleMedium)
                            }
                        }
                        Text(
                            destination.title,
                            color = labelColor,
                            style = MaterialTheme.typography.labelSmall,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GradTrackPageTopBar(
    title: String,
    onBack: () -> Unit,
    onSwitchWorkspace: () -> Unit = {}
) {
    val forest = Color(0xFF25463B)
    Surface(color = Color.White, shadowElevation = 1.dp) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(64.dp)
                .padding(horizontal = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.semantics { contentDescription = "Back" }
            ) {
                Text("←", color = forest, style = MaterialTheme.typography.headlineSmall)
            }
            Text(
                title,
                color = Color(0xFF27344C),
                style = MaterialTheme.typography.titleLarge,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 4.dp)
            )
            TextButton(onClick = onSwitchWorkspace) {
                Text("Switch", color = forest)
            }
        }
    }
}
