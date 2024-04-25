package com.booking.slots.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.booking.booking.BookingScreen
import com.booking.confirm.ConfirmationScreen
import com.booking.dashboard.ui.theme.DashboardRoute
import com.booking.dashboard.ui.theme.DashboardScreen
import com.booking.home.HomeScreen
import com.booking.login.LoginRoute
import com.booking.registration.RegistrationRoute

@Composable
fun NavigationHost(
    startDestination: String = Routes.Dashboard.route
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.Login.route) {
            LoginRoute {
                navController.navigate(Routes.Dashboard.route)
            }
        }
        composable(Routes.Register.route) {
            RegistrationRoute {
                navController.navigate(Routes.Home.route)
            }
        }
        composable(Routes.Home.route) {
            HomeScreen(
                navigateToLogin = { navController.navigate(Routes.Login.route) },
                navigateToRegister = { navController.navigate(Routes.Register.route) }
            )
        }
        composable(Routes.Dashboard.route) {
            DashboardRoute(navigateToBooking = {navController.navigate(Routes.Booking.route)})
        }
        composable(Routes.Booking.route) {
            BookingScreen()
        }
        composable(Routes.Confirm.route) {
            ConfirmationScreen()
        }
    }

}