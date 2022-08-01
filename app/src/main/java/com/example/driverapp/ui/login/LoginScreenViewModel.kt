package com.example.driverapp.ui.login

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.driverapp.data.Result
import com.example.driverapp.data.remote.response.LoggedInUser
import com.example.driverapp.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

/**
 * VM for Login Repo
 */
@HiltViewModel//let hilt know this is our vm
class LoginScreenViewModel @Inject constructor(
    private val loginRepo: LoginRepository
) : ViewModel() {
    var loadError = mutableStateOf("")
    var loggedInUser = mutableStateOf<LoggedInUser?>(null)
    var isLoading = mutableStateOf(true)

    fun login(username: String, password: String) {
        viewModelScope.launch {
            val result = loginRepo.login(username, password)
            when (result) {
                is Result.Error -> {
                    loadError.value = result.message ?: "Unknown Error"
                }
                is Result.Success -> {
                    isLoading.value = false
                    if (result.data != null) {
                        loggedInUser.value = result.data
                        Timber.i("We are logged in with: {${loggedInUser.value}}")
                    } else {
                        loadError.value = "Error logging in"
                        Timber.e("Successful api call, error logging in")
                    }
                    loadError.value = ""
                }
            }
        }
    }
}