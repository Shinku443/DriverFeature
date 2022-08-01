package com.example.driverapp.ui.drivers

import com.example.driverapp.data.remote.DriverApi
import com.example.driverapp.data.remote.response.CurrentLocation
import com.example.driverapp.data.remote.response.Details
import com.example.driverapp.data.remote.response.DriverListItem
import com.example.driverapp.data.repository.DriverRepository
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


@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
internal class DriverRepositoryModelTest {
    private val mockApi: DriverApi = mock(DriverApi::class.java)
    private lateinit var driverRepository: DriverRepository
    private lateinit var listOfDrivers: List<DriverListItem>

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        driverRepository = DriverRepository(mockApi)
        val currentLocation = CurrentLocation("1", "1")
        val details = Details(currentLocation, "XXXXXX", 2, 2, 4, "TOW")
        val defaultDriver = DriverListItem(details, "Michael", "Stevens", "999-999-0000")
        listOfDrivers = listOf(defaultDriver)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun ifDriverList_returnsList_thenReturnList() = runTest {
        Mockito.`when`(mockApi.getDriverList()).thenReturn(
            listOfDrivers
        )
        val actualValue = driverRepository.getListOfDrivers()
        advanceUntilIdle()
        Assert.assertEquals(listOfDrivers, actualValue.data)
    }

    @Test
    fun ifDriverList_returnsError_thenThrowException() = runTest {
        val expectedException: NullPointerException = NullPointerException()
        Mockito.`when`(mockApi.getDriverList()).thenThrow(NullPointerException::class.java)
        val actualValue = driverRepository.getListOfDrivers()
        advanceUntilIdle()
        Assert.assertEquals("Error: $expectedException", actualValue.message)
    }
}
