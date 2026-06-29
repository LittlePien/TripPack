package com.example.trippack.ui.navigation

sealed class Screen(val route: String) {
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Wishlist : Screen("wishlist")
    object AddDestination : Screen("add_destination")
    object DestinationDetail : Screen("destination_detail/{destinationId}") {
        fun createRoute(destinationId: Int) = "destination_detail/$destinationId"
    }
    object CreateTrip : Screen("create_trip?tripId={tripId}") {
        fun createRoute(tripId: Int? = null) =
            if (tripId != null) "create_trip?tripId=$tripId" else "create_trip"
    }
    object PackingList: Screen("packing_list/{tripId}") {
        fun createRoute(tripId: Int) = "packing_list/$tripId"
    }
    object TravelLog : Screen("travel_log")
    object Profile : Screen("profile")
}

data class BottomNavItem(
    val screen: Screen,
    val label: String,
    val icon: androidx.compose.ui.graphics.vector.ImageVector
)