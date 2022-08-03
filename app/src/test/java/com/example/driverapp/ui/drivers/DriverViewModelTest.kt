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
    private lateinit var driverViewModel: DriverViewModel
    private val currentLocation = CurrentLocation("1", "1")
    private val details = Details(currentLocation, "XXXXXX", 2, 2, 4, "TOW")
    private val defaultDriver = DriverListItem(details, "Michael", "Stevens", "999-999-0000")
    private var listOfDrivers: List<DriverListItem> = listOf(defaultDriver)

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

    private fun setupDataForViewModel() {
        driverViewModel = DriverViewModel(mockDriverRepository)
    }

    @Test
    fun getValidDriverList_expectEquals() = runTest {
        `when`(mockDriverRepository.getListOfDrivers()).thenReturn(
            Result.Success(listOfDrivers)
        )
        setupDataForViewModel()
        advanceUntilIdle()

        Assert.assertEquals(listOfDrivers, driverViewModel.driverList.value)
    }

    @Test
    fun getNullDriverListOnSuccessfulCall_expectError() = runTest {
        `when`(mockDriverRepository.getListOfDrivers()).thenReturn(
            Result.Success(listOf())
        )
        setupDataForViewModel()
        advanceUntilIdle()

        Assert.assertEquals("Error loading driver list", driverViewModel.loadError.value)
    }

    @Test
    fun getInvalidDriverList_expectNotEquals() = runTest {
        val expectedList = listOf(null)
        `when`(mockDriverRepository.getListOfDrivers()).thenReturn(
            Result.Success(listOfDrivers)
        )
        setupDataForViewModel()
        advanceUntilIdle()
        Assert.assertNotEquals(expectedList, mockDriverRepository.getListOfDrivers().data)
    }

    @Test
    fun getErrorWhenCallingRepo() = runTest {
        val expectedError = "Error occurred"
        `when`(mockDriverRepository.getListOfDrivers()).thenReturn(
            Result.Error("Error occurred", listOfDrivers)
        )
        setupDataForViewModel()
        advanceUntilIdle()

        Assert.assertEquals(expectedError, driverViewModel.loadError.value)
    }
}