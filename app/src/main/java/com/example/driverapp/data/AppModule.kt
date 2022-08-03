package com.example.driverapp.data

import com.example.driverapp.data.remote.DriverApi
import com.example.driverapp.data.remote.LoginApi
import com.example.driverapp.data.repository.DriverRepository
import com.example.driverapp.data.repository.LoginRepository
import com.example.driverapp.util.Constants.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

/**
 * Handles all of our DI (dagger/hilt)
 * for repo/api handling
 */
@Module
@InstallIn(SingletonComponent::class)
class AppModule {
    @Singleton
    @Provides
    fun provideLoginRepo(
        api: LoginApi,
    ) = LoginRepository(api)

    @Singleton
    @Provides
    fun provideLoginApi(): LoginApi {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient = OkHttpClient.Builder()
        httpClient.addInterceptor(logging)

        return Retrofit.Builder()
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(LoginApi::class.java)
    }


    @Singleton
    @Provides
    fun provideDriverRepo(
        api: DriverApi,
    ) = DriverRepository(api)

    @Singleton
    @Provides
    fun provideDriverApi(): DriverApi {
        return Retrofit.Builder()
            .addConverterFactory(GsonConverterFactory.create())
            .baseUrl(BASE_URL)
            .build()
            .create(DriverApi::class.java)
    }

}