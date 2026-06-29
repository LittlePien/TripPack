package com.example.trippack.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.trippack.ui.auth.LoginScreen
import com.example.trippack.ui.auth.RegisterScreen
import com.example.trippack.ui.destination.DestinationDetailScreen
import com.example.trippack.ui.home.HomeScreen
import com.example.trippack.ui.packing.PackingListScreen
import com.example.trippack.ui.profile.ProfileScreen
import com.example.trippack.ui.travellog.TravelLogScreen
import com.example.trippack.ui.trip.CreateTripScreen
import com.example.trippack.ui.wishlist.AddDestinationScreen
import com.example.trippack.ui.wishlist.WishlistScreen
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    val startDestination = if (FirebaseAuth.getInstance().currentUser != null) {
        Screen.Home.route
    } else {
        Screen.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(Screen.Login.route) {
             LoginScreen(
                 onLoginSuccess = { navController.navigate(Screen.Home.route) { popUpTo(Screen.Login.route) { inclusive = true } } },
                 onNavigateToRegister = { navController.navigate(Screen.Register.route) }
             )
        }
        composable(Screen.Register.route) {
             RegisterScreen(
                 onRegisterSuccess = { navController.navigate(Screen.Home.route) { popUpTo(Screen.Login.route) { inclusive = true } } },
                 onNavigateBack = { navController.popBackStack() }
             )
        }
        composable(Screen.Home.route) {
            HomeScreen(
                onNavigateToWishlist = {navController.navigate(Screen.Wishlist.route)},
                onNavigateToPackingList = {tripId ->
                    navController.navigate(Screen.PackingList.createRoute(tripId))
                }
            )
        }
        composable(Screen.Wishlist.route) {
            WishlistScreen(
                onNavigateToAddDestination = { navController.navigate(Screen.AddDestination.route) },
                onNavigateToDestinationDetail = { id -> navController.navigate(Screen.DestinationDetail.createRoute(id)) }
            )
        }
        composable(Screen.AddDestination.route) {
            AddDestinationScreen(
                onSaveSuccess = {navController.popBackStack()},
                onNavigateBack = {navController.popBackStack()}
            )
        }
        composable(Screen.DestinationDetail.route) {
            DestinationDetailScreen(
                onNavigateBack = {navController.popBackStack()}
            )
        }
        composable(
            Screen.CreateTrip.route,
            arguments = listOf(navArgument("tripId") {
                type = NavType.StringType
                nullable = true
                defaultValue = null
            })
        ) {
            CreateTripScreen(
                onTripCreated = { tripId ->
                    navController.navigate(Screen.PackingList.createRoute(tripId)) {
                        popUpTo(Screen.CreateTrip.route) {inclusive = true}
                    }
                },
                onNavigateBack = {navController.popBackStack()}
            )

        }
        composable(Screen.PackingList.route) {
            PackingListScreen()
        }
        composable(Screen.TravelLog.route) {
            TravelLogScreen(
                onNavigateToPackingList = { tripId ->
                    navController.navigate(Screen.PackingList.createRoute(tripId))
                },
                onNavigateToEditTrip = { tripId ->
                    navController.navigate(Screen.CreateTrip.createRoute(tripId))
                }
            )
        }
        composable(Screen.Profile.route) {
            ProfileScreen(
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}