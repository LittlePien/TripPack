package com.example.trippack.ui.navigation

import kotlinx.serialization.Serializable

@Serializable object Login
@Serializable object Register
@Serializable object Home
@Serializable object Wishlist
@Serializable object AddDestination
@Serializable object TravelLog
@Serializable object Profile
@Serializable object Settings

@Serializable data class DestinationDetail(val destinationId: Int)
@Serializable data class PackingList(val tripId: Int)
@Serializable data class CreateTrip(val tripId: Int? = null)