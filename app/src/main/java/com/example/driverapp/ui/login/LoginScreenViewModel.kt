package com.example.driverapp.ui.login

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.driverapp.data.Result
import com.example.driverapp.data.remote.response.LoggedInUser
import com.example.driverapp.data.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel//let hilt know this is our vm
class LoginScreenViewModel @Inject constructor(
    private val loginRepo: LoginRepository
) : ViewModel() {
    var defaultUser = LoggedInUser(-1, "unknown", "unknown", "unknown")
    var loadError = mutableStateOf("")
    var loggedInUser = mutableStateOf(defaultUser)

    suspend fun login(username: String, password: String) {
        val result = loginRepo.login(username, password)
        when (result) {
            is Result.Error -> {
                loadError.value = result.message!!
                loggedInUser.value = defaultUser //just in case we will reset to a non-logged in state
            }
            is Result.Success -> {
                loggedInUser.value = result.data!!
                loadError.value = "" //reset just in case, since we are using this for our toast
            }
        }
    }
}