package com.example.trippack

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.trippack.ui.components.BottomNavBar
import com.example.trippack.ui.navigation.CreateTrip
import com.example.trippack.ui.navigation.Home
import com.example.trippack.ui.navigation.NavGraph
import com.example.trippack.ui.navigation.Profile
import com.example.trippack.ui.navigation.TravelLog
import com.example.trippack.ui.navigation.Wishlist
import com.example.trippack.ui.theme.TripPackTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        requestNotificationPermission()
        setContent {
            TripPackTheme {
                val navController = rememberNavController()
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val showBottomBar = navBackStackEntry?.destination?.let { dest ->
                    dest.hasRoute(Home::class) || dest.hasRoute(Wishlist::class) ||
                            dest.hasRoute(CreateTrip::class) || dest.hasRoute(TravelLog::class) ||
                            dest.hasRoute(Profile::class)
                } ?: false

                Scaffold(
                    bottomBar = { if (showBottomBar) BottomNavBar(navController) }
                ) { padding ->
                    NavGraph(navController = navController, modifier = Modifier.padding(padding))
                }
            }
        }
    }

    private fun requestNotificationPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
        }
    }
}