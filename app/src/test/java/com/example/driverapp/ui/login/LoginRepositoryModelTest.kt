package com.example.driverapp.ui.login

import com.example.driverapp.data.remote.LoginApi
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
import org.mockito.Mockito.any
import org.mockito.Mockito.mock
import org.mockito.kotlin.anyOrNull


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
internal class LoginRepositoryModelTest {
    private val mockApi: LoginApi = mock(LoginApi::class.java)
    private lateinit var loginRepository: LoginRepository
    private var loggedInUser = LoggedInUser(10, "Michael", "Stevens", "msteven")

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        loginRepository = LoginRepository(mockApi)

    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun ifSuccessfulLogin_then_returnLoggedInUser() = runTest {
        Mockito.`when`(mockApi.login(anyOrNull(), anyOrNull(), anyOrNull())).thenReturn(
            loggedInUser
        )
        val actualValue = loginRepository.login("5", "5")
        advanceUntilIdle()
        Assert.assertEquals(loggedInUser, actualValue.data)
    }

    @Test
    fun ifFailedLogin_then_returnError() = runTest {
        val expectedException: NullPointerException = NullPointerException()
        Mockito.`when`(mockApi.login(anyOrNull(), anyOrNull(), anyOrNull())).thenThrow(NullPointerException::class.java)
        val actualValue = loginRepository.login("4", "4")
        advanceUntilIdle()
        Assert.assertEquals("An unknown error has occurred: $expectedException", actualValue.message)
    }
}
