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
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth

@Composable
fun NavGraph(navController: NavHostController, modifier: Modifier = Modifier) {
    val startDestination: Any = if (FirebaseAuth.getInstance().currentUser != null) Home else Login

    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable<Login> {
            LoginScreen(
                onLoginSuccess = {
                    navController.navigate(Home) {
                        popUpTo<Login> { inclusive = true }
                    }
                },
                onNavigateToRegister = { navController.navigate(Register)}
            )
        }

        composable<Register> {
            RegisterScreen(
                onRegisterSuccess = {
                    navController.navigate(Home) {
                        popUpTo<Login>{ inclusive = true }
                    }
                },
                onNavigateBack = {navController.popBackStack()}
            )
        }

        composable<Home> {
            HomeScreen(
                onNavigateToWishlist = { navController.navigate(Wishlist) },
                onNavigateToPackingList = { tripId ->
                    navController.navigate(PackingList(tripId = tripId))
                }
            )
        }

        composable<Wishlist> {
            WishlistScreen(
                onNavigateToAddDestination = { navController.navigate(AddDestination) },
                onNavigateToDestinationDetail = { id ->
                    navController.navigate(DestinationDetail(destinationId = id))
                }
            )
        }

        composable<AddDestination> {
            AddDestinationScreen(
                onSaveSuccess = { navController.popBackStack() },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<DestinationDetail> {
            DestinationDetailScreen(
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<CreateTrip> {
            CreateTripScreen(
                onTripCreated = { tripId ->
                    navController.navigate(PackingList(tripId = tripId)) {
                        popUpTo<CreateTrip> { inclusive = true }
                    }
                },
                onNavigateBack = { navController.popBackStack() }
            )
        }

        composable<PackingList> {
            PackingListScreen(
                onNavigateBack = {navController.popBackStack()}
            )
        }

        composable<TravelLog> {
            TravelLogScreen(
                onNavigateToPackingList = { tripId ->
                    navController.navigate(PackingList(tripId = tripId))
                },
                onNavigateToEditTrip = { tripId ->
                    navController.navigate(CreateTrip(tripId = tripId))
                }
            )
        }

        composable<Profile> {
            ProfileScreen(
                onLogout = {
                    navController.navigate(Login) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }
    }
}