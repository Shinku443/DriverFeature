package com.example.driverapp.data.repository

import com.example.driverapp.BuildConfig
import com.example.driverapp.data.Result
import com.example.driverapp.data.remote.LoginApi
import com.example.driverapp.data.remote.response.LoggedInUser
import dagger.hilt.android.scopes.ActivityScoped
import javax.inject.Inject


/**
 * Simple Repo that will handle login api call
 */
@ActivityScoped
class LoginRepository @Inject constructor(private val loginApi: LoginApi) {
    suspend fun login(username: String, password: String): Result<LoggedInUser> {
        val result = try {
            loginApi.login(BuildConfig.clientId, username, password)//pass in clientId
        } catch (e: Exception) {
            return Result.Error("An unknown error has occurred: ${e.message}")
        }
        return Result.Success(result)
    }
}