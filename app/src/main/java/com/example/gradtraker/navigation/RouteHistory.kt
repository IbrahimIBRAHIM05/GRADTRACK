package com.example.gradtraker.navigation

private const val ROUTE_SEPARATOR = "\u001F"

object RouteHistory {
    fun current(encodedRoutes: String): String = encodedRoutes.split(ROUTE_SEPARATOR).last()

    fun canGoBack(encodedRoutes: String): Boolean = encodedRoutes.contains(ROUTE_SEPARATOR)

    fun push(encodedRoutes: String, route: String): String {
        if (current(encodedRoutes) == route) return encodedRoutes
        return "$encodedRoutes$ROUTE_SEPARATOR$route"
    }

    fun pop(encodedRoutes: String): String {
        val routes = encodedRoutes.split(ROUTE_SEPARATOR)
        return if (routes.size > 1) routes.dropLast(1).joinToString(ROUTE_SEPARATOR) else encodedRoutes
    }
}
