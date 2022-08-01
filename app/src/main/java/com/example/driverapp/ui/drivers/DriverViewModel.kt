package com.example.driverapp.ui.drivers

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.driverapp.data.Result
import com.example.driverapp.data.remote.response.DriverListItem
import com.example.driverapp.data.repository.DriverRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * VM of Driver Logic - tracks list from API
 */
@HiltViewModel
class DriverViewModel @Inject constructor(
    private val driverRepo: DriverRepository
) : ViewModel() {
    var driverList =
        mutableStateOf<List<DriverListItem>>(listOf())
    var loadError = mutableStateOf("")
    var isLoading = mutableStateOf(false)

    init {
        getDriverList()
    }

    private fun getDriverList() {
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
                    if (entries != null) {
                        loadError.value = ""
                        driverList.value = entries.sortedBy {
                            it.lastName
                        }
                    } else {
                        loadError.value = "Error loading driver list"
                        Timber.e("Successful api call, error pulling driver list")
                    }
                }
                is Result.Error -> loadError.value = result.message ?: "Unknown error"
            }
        }
    }
}
