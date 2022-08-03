package com.example.driverapp.data.repository

import com.example.driverapp.data.Result
import com.example.driverapp.data.remote.DriverApi
import com.example.driverapp.data.remote.response.DriverListItem
import javax.inject.Inject

/**
 * Driver repo that will handle grabbing the driver list
 */
class DriverRepository @Inject constructor(private val driverApi: DriverApi) {
    suspend fun getListOfDrivers(): Result<List<DriverListItem>> {
        val response = try {
            driverApi.getDriverList()
        } catch (e: Exception) {
            return Result.Error("Error: $e")
        }
        return Result.Success(response)
    }
}