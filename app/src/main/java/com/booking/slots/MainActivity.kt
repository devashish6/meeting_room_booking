package com.booking.slots

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.booking.slots.navigation.NavigationHost
import com.booking.slots.navigation.Routes
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val mainViewModel : MainViewModel = hiltViewModel()
            val isLoggedIn = mainViewModel.isLoggedIn.collectAsStateWithLifecycle()
            val userName = mainViewModel.userName.collectAsStateWithLifecycle()
            var startDestination = Routes.Home.route
            if (isLoggedIn.value) {
                startDestination = Routes.Dashboard.route
            }
            NavigationHost(startDestination = startDestination, userName.value)
        }
    }
}