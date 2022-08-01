package com.example.driverapp.ui.drivers

import com.example.driverapp.data.Result
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
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(JUnit4::class)
internal class DriverViewModelTest {
    private val mockDriverRepository: DriverRepository = mock(DriverRepository::class.java)
    private lateinit var driverViewModel: DriverViewModel //Because we have the getDriverList call in init we need coroutine

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    @BeforeEach
    fun setup() {
        Dispatchers.setMain(StandardTestDispatcher())
        driverViewModel = DriverViewModel(mockDriverRepository)
    }

    @AfterEach
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun getValidDriverList_expectEquals() = runTest {
        val currentLocation = CurrentLocation("1", "1")
        val details = Details(currentLocation, "XXXXXX", 2, 2, 4, "TOW")
        val defaultDriver = DriverListItem(details, "Michael", "Stevens", "999-999-0000")
        val listOfDrivers = listOf(defaultDriver)

        `when`(mockDriverRepository.getListOfDrivers()).thenReturn(
            Result.Success(listOfDrivers)
        )
        callPrivateMethodForDriverList()
        advanceUntilIdle()

        Assert.assertEquals(listOfDrivers, driverViewModel.driverList.value)
    }

    @Test
    fun getInvalidDriverList_expectNotEquals() = runTest {
        val currentLocation = CurrentLocation("1", "1")
        val details = Details(currentLocation, "XXXXXX", 2, 2, 4, "TOW")
        val defaultDriver = DriverListItem(details, "Michael", "Stevens", "999-999-0000")
        val listOfDrivers = listOf(defaultDriver)
        val expectedList = listOf(null)
        `when`(mockDriverRepository.getListOfDrivers()).thenReturn(
            Result.Success(listOfDrivers)
        )

        callPrivateMethodForDriverList()
        advanceUntilIdle()
        Assert.assertNotEquals(expectedList, mockDriverRepository.getListOfDrivers().data)
    }

    @Test
    fun getErrorWhenCallingRepo() = runTest {
        val currentLocation = CurrentLocation("1", "1")
        val details = Details(currentLocation, "XXXXXX", 2, 2, 4, "TOW")
        val defaultDriver = DriverListItem(details, "Michael", "Stevens", "999-999-0000")
        val listOfDrivers = listOf(defaultDriver)

        val expectedError = "Error occurred"
        `when`(mockDriverRepository.getListOfDrivers()).thenReturn(
            Result.Error("Error occurred", listOfDrivers)
        )
        callPrivateMethodForDriverList()
        advanceUntilIdle()

        Assert.assertEquals(expectedError, driverViewModel.loadError.value)


    }

    fun callPrivateMethodForDriverList() {
        val method = driverViewModel.javaClass.getDeclaredMethod("getDriverList")
        method.isAccessible = true
        method.invoke(driverViewModel)
    }
}