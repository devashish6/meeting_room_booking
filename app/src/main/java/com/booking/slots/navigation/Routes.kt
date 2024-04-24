package com.booking.slots.navigation

sealed class Routes(val route: String) {
    data object Login : Routes("Login")
    data object Register : Routes("Register")
    data object Home : Routes("Home")
    data object Dashboard : Routes("Dashboard")
    data object Confirm : Routes("Confirm")
    data object Booking : Routes("Booking")
}