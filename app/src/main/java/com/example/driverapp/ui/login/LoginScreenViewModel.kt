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
    private var defaultUser = LoggedInUser(-1, "unknown", "unknown", "unknown")
    var loadError = mutableStateOf("")
    var loggedInUser = mutableStateOf(defaultUser)

    fun login(username: String, password: String) {
        loggedInUser.value =
            defaultUser //any attempt to login resets us to an invalid logged in user
        viewModelScope.launch {
            val result = loginRepo.login(username, password)
            when (result) {
                is Result.Error -> {
                    loadError.value = result.message ?: "Unknown Error"
                }
                is Result.Success -> {
                    Timber.i("We are logged in with: {$loggedInUser}")
                    loggedInUser.value = result.data!!
                    loadError.value = "" //reset just in case, since we are using this for our toast
                }
            }
        }
    }
}