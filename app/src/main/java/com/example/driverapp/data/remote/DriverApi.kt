package com.example.driverapp.data.remote

import com.example.driverapp.data.remote.response.DriverListItem
import retrofit2.http.GET
import retrofit2.http.Headers

/**
 * Handles Driver List
 */
interface DriverApi {
    @GET("/drivers")
    suspend fun getDriverList(): List<DriverListItem>
}