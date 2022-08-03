package com.example.driverapp.data.remote

import com.example.driverapp.data.remote.response.LoggedInUser
import retrofit2.http.*

/**
 * Class that handles authentication with
 * login credentials and retrieves user information.
 */
interface LoginApi {
    @Headers(
        "Content-Type:application/json"
    )
    @POST("/login")
    @FormUrlEncoded
    suspend fun login(
        @Header("clientId") clientId: String?,
        @Field("username") username: String,
        @Field("password") password: String
    ): LoggedInUser
}