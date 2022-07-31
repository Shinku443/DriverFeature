package com.example.driverapp.ui.drivers

import com.example.driverapp.data.Result
import com.example.driverapp.data.remote.response.CurrentLocation
import com.example.driverapp.data.remote.response.Details
import com.example.driverapp.data.remote.response.DriverListItem
import com.example.driverapp.data.repository.DriverRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.jupiter.api.Test
import org.mockito.Mockito.`when`
import org.mockito.Mockito.mock

internal class DriverViewModelTest {

    private val mockDriverRepository: DriverRepository = mock(DriverRepository::class.java)

    @Test
    fun getValidDriverList_expectEquals() {
        val currentLocation = CurrentLocation("1", "1")
        val details = Details(currentLocation, "XXXXXX", 2, 2, 4, "TOW")
        val defaultDriver = DriverListItem(details, "Michael", "Stevens", "999-999-0000")
        val listOfDrivers = listOf(defaultDriver)
        val expectedList = listOf(defaultDriver)
        runBlocking {
            `when`(mockDriverRepository.getListOfDrivers()).thenReturn(
                Result.Success(listOfDrivers)
            )
            Assert.assertEquals(expectedList, mockDriverRepository.getListOfDrivers().data)
        }
    }

    @Test
    fun getInvalidDriverList_expectNotEquals() {
        val currentLocation = CurrentLocation("1", "1")
        val details = Details(currentLocation, "XXXXXX", 2, 2, 4, "TOW")
        val defaultDriver = DriverListItem(details, "Michael", "Stevens", "999-999-0000")
        val listOfDrivers = listOf(defaultDriver)
        val expectedList = listOf(null)
        runBlocking {
            `when`(mockDriverRepository.getListOfDrivers()).thenReturn(
                Result.Success(listOfDrivers)
            )
            Assert.assertNotEquals(expectedList, mockDriverRepository.getListOfDrivers().data)
        }
    }

    @Test
    fun getErrorWhenCallingRepo() {
        val currentLocation = CurrentLocation("1", "1")
        val details = Details(currentLocation, "XXXXXX", 2, 2, 4, "TOW")
        val defaultDriver = DriverListItem(details, "Michael", "Stevens", "999-999-0000")
        val listOfDrivers = listOf(defaultDriver)

        val expectedError: String = "Error occurred"
        runBlocking {
            `when`(mockDriverRepository.getListOfDrivers()).thenReturn(
                Result.Error("Error occurred", listOfDrivers)
            )
            Assert.assertEquals(expectedError, mockDriverRepository.getListOfDrivers().message)

        }

    }
}