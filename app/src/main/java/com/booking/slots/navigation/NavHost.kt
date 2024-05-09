package com.booking.slots.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.booking.booking.BookingsRoute
import com.booking.dashboard.ui.theme.DashboardRoute
import com.booking.home.HomeScreen
import com.booking.login.LoginRoute
import com.booking.registration.RegistrationRoute

@Composable
fun NavigationHost(
    startDestination: String = Routes.Home.route,
    userName: String
) {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = startDestination) {
        composable(Routes.Login.route) {
            LoginRoute(
                onSuccessfulLogin = { navController.navigate(Routes.Dashboard.route) },
                onBackClicked = { navController.navigateUp() }
            )
        }
        composable(Routes.Register.route) {
            RegistrationRoute (
                onSuccessfulRegistration = { navController.navigate(Routes.Home.route) },
                onBackClicked = { navController.navigateUp() }
            )
        }
        composable(Routes.Home.route) {
            HomeScreen(
                navigateToLogin = { navController.navigate(Routes.Login.route) },
                navigateToRegister = { navController.navigate(Routes.Register.route) }
            )
        }
        composable(Routes.Dashboard.route) {
            DashboardRoute(
                navigateToBooking = {navController.navigate(Routes.Booking.route)},
                navigateToHome = { navController.navigate(Routes.Home.route)}
            )
        }
        composable(Routes.Booking.route) {
            BookingsRoute(
                navigateToDashboard = { navController.navigate(Routes.Dashboard.route)},
                onBackClicked = { navController.navigateUp() },
                userName = userName
            )
        }
    }

}