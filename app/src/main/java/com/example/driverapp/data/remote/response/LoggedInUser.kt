package com.example.driverapp.data.remote.response

data class LoggedInUser(
    val id: Int,
    val first: String,
    val last: String,
    val username: String
)