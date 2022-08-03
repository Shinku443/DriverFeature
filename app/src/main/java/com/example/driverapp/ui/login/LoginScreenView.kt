package com.example.driverapp.ui.login

import android.widget.Toast
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import com.example.driverapp.util.Constants
import timber.log.Timber


/**
 * View for Login Screen
 */
@Composable
fun LoginScreen(
    navController: NavController,
    viewModel: LoginScreenViewModel = hiltViewModel(),
) {
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var username by remember { mutableStateOf("") }
    val loadError by remember { viewModel.loadError }
    val loggedInUser by remember { viewModel.loggedInUser }
    val isLoading by remember { viewModel.isLoading }

    Column(
        Modifier.fillMaxSize()
    ) {

        SubcomposeAsyncImage(
            model = ImageRequest.Builder(LocalContext.current)
                .data(
                    when (isSystemInDarkTheme()) {
                        true -> Constants.IMG_LOGO_URL_DARK
                        false -> Constants.IMG_LOGO_URL_LIGHT
                    }
                )
                .build(),
            contentDescription = "mastery_logo",
            modifier = Modifier
                .padding(top = 40.dp)
                .align(CenterHorizontally),
            onError = {
                Timber.e("Failed to load image! : $it.result.throwable.message")
            }
        )
        Text(
            textAlign = TextAlign.Center,
            text = "Welcome to the DriverList App \n Please login:",
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(horizontal = 16.dp, vertical = 40.dp)
        )

        TextField(
            value = username,
            onValueChange = {
                username = it
            },
            placeholder = { Text("Username") },
            singleLine = true,
            modifier = Modifier.align(CenterHorizontally)
        )
        Spacer(Modifier.height(20.dp))
        TextField(
            value = password,
            onValueChange = {
                password = it
            },
            label = { Text("Password") },
            singleLine = true,
            placeholder = { Text("Password") },
            modifier = Modifier.align(CenterHorizontally),
            visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            trailingIcon = {
                val image = if (passwordVisible)
                    Icons.Filled.Visibility
                else Icons.Filled.VisibilityOff

                // Please provide localized description for accessibility services
                val description = if (passwordVisible) "Hide password" else "Show password"

                IconButton(onClick = { passwordVisible = !passwordVisible }) {
                    Icon(imageVector = image, description)
                }
            }
        )


        Button(
            modifier = Modifier
                .align(CenterHorizontally)
                .padding(
                    horizontal = 20.dp,
                    vertical = 20.dp
                ),
            onClick = {
                if (username.isNotEmpty() && password.isNotEmpty()) { //only make api call if we have username/pw
                    try {
                        viewModel.login(username, password)
                    } catch (e: Exception) {
                        Timber.e("loadErr: $loadError with exception:: $e")
                    }
                } else {
                    viewModel.loadError.value = "Invalid login"
                }
            },
        ) {
            LaunchedEffect(isLoading) {
                if (!isLoading) {
                    navController.popBackStack()
                    navController.navigate("driver_list_screen")
                }
            }
            Text(text = "Login")
            if (loadError.isNotEmpty()) {
                Timber.e("Load error:: $loadError")
                Toast.makeText(
                    LocalContext.current,
                    "Please enter a valid login!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
    }
}