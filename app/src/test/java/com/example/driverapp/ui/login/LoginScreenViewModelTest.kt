package com.example.driverapp.ui.login

import com.example.driverapp.data.Result
import com.example.driverapp.data.remote.response.LoggedInUser
import com.example.driverapp.data.repository.LoginRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any

internal class LoginScreenViewModelTest {
    private val mockLoginRepository: LoginRepository = Mockito.mock(LoginRepository::class.java)

    @Test
    fun ifLoginSuccessful_expectValidUser() {
        val loggedInUser = LoggedInUser(15, "Michael", "Stevens", "mstevens")
        runBlocking {
            Mockito.`when`(mockLoginRepository.login(any(), any())).thenReturn(
                Result.Success(loggedInUser)
            )
            Assert.assertEquals(loggedInUser, mockLoginRepository.login("ooga", "booga").data)
        }
    }

    @Test
    fun ifLoginNotSuccessful_expectInvalidUser() {
        val loggedInUser = LoggedInUser(15, "Michael", "Stevens", "mstevens")
        runBlocking {
            Mockito.`when`(mockLoginRepository.login(any(), any())).thenReturn(
                Result.Error("Error", null)
            )
            Assert.assertNotEquals(loggedInUser, mockLoginRepository.login("ooga", "booga").data)
        }
    }
}