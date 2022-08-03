package com.example.driverapp.data.remote.response

data class Details(
    val currentLocation: CurrentLocation,
    val plateNumber: String,
    val trailerHeight: Int,
    val trailerLength: Int,
    val trailerWidth: Int,
    val trailerType: String
)