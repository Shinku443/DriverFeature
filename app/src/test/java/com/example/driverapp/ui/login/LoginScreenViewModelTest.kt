package com.example.driverapp.ui.login

import com.example.driverapp.data.Result
import com.example.driverapp.data.remote.response.LoggedInUser
import com.example.driverapp.data.repository.LoginRepository
import com.example.driverapp.util.MainCoroutineRule
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.Assert
import org.junit.Rule
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4
import org.mockito.Mockito
import org.mockito.Mockito.mock
import org.mockito.kotlin.any

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
internal class LoginScreenViewModelTest {
    private val mockLoginRepository: LoginRepository = mock(LoginRepository::class.java)
    private val loginViewModel = LoginScreenViewModel(mockLoginRepository)

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun ifLoginSuccessful_expectValidUser() = runTest {
        val loggedInUser = LoggedInUser(15, "Michael", "Stevens", "mstevens")

        Mockito.`when`(mockLoginRepository.login(any(), any())).thenReturn(
            Result.Success(loggedInUser)
        )
        loginViewModel.login("test", "test")
        advanceUntilIdle()
        Assert.assertEquals(loggedInUser, loginViewModel.loggedInUser.value)
    }


    @Test
    fun ifLoginNotSuccessful_expectInvalidUser() = runTest {

        val loggedInUser = LoggedInUser(15, "Michael", "Stevens", "mstevens")
        Mockito.`when`(mockLoginRepository.login(any(), any())).thenReturn(
            Result.Error("Error", null)
        )
        loginViewModel.login("test", "test")
        advanceUntilIdle()

        Assert.assertNotEquals(loggedInUser, loginViewModel.loggedInUser)
        Assert.assertEquals("Error", loginViewModel.loadError.value)
    }
}