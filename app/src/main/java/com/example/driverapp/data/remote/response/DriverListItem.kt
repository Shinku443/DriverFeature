package com.example.driverapp.data.remote.response

data class DriverListItem(
    val details: Details,
    val firstName: String,
    val lastName: String,
    val phoneNumber: String
)