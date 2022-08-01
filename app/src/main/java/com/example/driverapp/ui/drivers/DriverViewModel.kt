package com.example.driverapp.ui.drivers

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.driverapp.data.Result
import com.example.driverapp.data.remote.response.DriverListItem
import com.example.driverapp.data.repository.DriverRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * VM of Driver Logic - tracks list from API
 */
@HiltViewModel//let hilt know this is our vm
class DriverViewModel @Inject constructor(
    private val driverRepo: DriverRepository
) : ViewModel() {
    var driverList =
        mutableStateOf<List<DriverListItem>>(listOf()) //init a mutable list of driverlistitems which will house our data
    var loadError = mutableStateOf("")//use this to track error
    var isLoading = mutableStateOf(false)

    init { //we always want a driver list
        getDriverList()
    }

    private fun getDriverList() {
        //making network call so we want to use coroutine call from vm
        viewModelScope.launch {
            isLoading.value = true
            val result = driverRepo.getListOfDrivers()
            when (result) {
                is Result.Success -> {
                    val entries = result.data?.mapIndexed { index, driverListItem ->
                        DriverListItem(
                            driverListItem.details,
                            driverListItem.firstName,
                            driverListItem.lastName,
                            driverListItem.phoneNumber
                        )
                    }
                    isLoading.value = false
                    loadError.value = ""
                    entries?.let {
                        driverList.value += entries.sortedBy {
                            it.lastName
                        }
                    }
                }
                is Result.Error -> loadError.value = result.message ?: "Unknown error"
            }
        }
    }
}
