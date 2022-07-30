package com.example.driverapp

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.runtime.remember
import androidx.compose.ui.graphics.Color
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.driverapp.ui.drivers.DriverIndividualItemScreen
import com.example.driverapp.ui.drivers.DriverListScreen
import com.example.driverapp.ui.login.LoginScreen
import com.example.driverapp.ui.theme.DriverAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint //use this for daggerhilt injection
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DriverAppTheme {
                val navController =
                    rememberNavController() //Have to setup nav controller out here otherwise
                //rememberNavController will return a different nav controller inside the composable
                NavHost(
                    navController = navController,
                    startDestination = "login_screen"
                ) {
                    composable("login_screen") {
                        LoginScreen(navController = navController)//pass in navcontroller for when we switch screens
                    }
                    composable("driver_list_screen") {
                        DriverListScreen(navController = navController)
                    }

                    composable("driver_individual_item_screen/{firstName}/{lastName}",
                        arguments = listOf(
                            navArgument("firstName") {
                                type = NavType.StringType
                            },
                            navArgument("lastName") {
                                type = NavType.StringType
                            }
                        )
                    )
                    {

                        val firstName = remember {
                            it.arguments?.getString("firstName")
                        }

                        val lastName = remember {
                            it.arguments?.getString("lastName")
                        }

                        DriverIndividualItemScreen(
                            firstName = firstName ?: "",
                            lastName = lastName ?: "",
                            navController = navController
                        )
                    }
                }
            }
        }
    }
}