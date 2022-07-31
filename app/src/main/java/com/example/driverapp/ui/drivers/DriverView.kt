package com.example.driverapp.ui.drivers

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.DefaultShadowColor
import androidx.compose.ui.graphics.colorspace.Rgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.driverapp.data.remote.response.DriverListItem

/**
 * View for Drivers
 */
@Composable
fun DriverListScreen(
    navController: NavController,
    viewModel: DriverViewModel = hiltViewModel()
) {
    val driverList by remember { viewModel.driverList }

    //lazy column that will load all drivers
    LazyColumn(contentPadding = PaddingValues(16.dp)) {//equivalent of RecyclerView
        items(driverList) {
            //Display Item
            DriverItem(navController, it, viewModel)
            Spacer(Modifier.height(16.dp))
        }
    }
}

//Individual Driver Info to display in the list
@Composable
fun DriverItem(
    navController: NavController,
    driverListItem: DriverListItem,
    viewModel: DriverViewModel = hiltViewModel()
) {
    val isLoading by remember { viewModel.isLoading }
    Column(
        Modifier
            .clip(RoundedCornerShape(5.dp))
            .fillMaxSize()
            .height(100.dp)
            .background(
                when (isSystemInDarkTheme()) {
                    true -> Color.Black
                    false -> Color.LightGray
                }
            )
            .clickable {
                navController.navigate(
                    "driver_individual_item_screen/${driverListItem.firstName}/${driverListItem.lastName}"
                )
            },
        verticalArrangement = Arrangement.Center,
    ) {
        if (isLoading) {
            CircularProgressIndicator(color = MaterialTheme.colors.primary)
        } else {
            //We sorted by last name so display last and then first
            Row(
                Modifier
                    .padding(start = 20.dp)
            ) {
                Text(
                    text = "Last Name: ",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = driverListItem.lastName
                )
            }
            Spacer(Modifier.height(10.dp))
            Row(
                Modifier
                    .padding(start = 20.dp)
            ) {
                Text(
                    text = "First Name:",
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.width(2.dp))
                Text(
                    text = driverListItem.firstName
                )
            }
        }
    }

}


@Composable
fun TopBar(
    navController: NavController,
    modifier: Modifier = Modifier,
) {
    Box(
        contentAlignment = Alignment.TopStart,
    ) {
        Icon(imageVector = Icons.Default.ArrowBack,
            contentDescription = null,
            tint = Color.Black,
            modifier = Modifier
                .size(36.dp)
                .offset(16.dp, 16.dp)
                .clickable {
                    navController.popBackStack()
                }
        )
    }
}

@Composable
fun DriverItemDetails(
    navController: NavController,
    driverListItem: DriverListItem
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        TopBar(
            navController = navController,
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.TopCenter)
        )

        Column(
            Modifier
                .padding(
                    top = 60.dp,
                    start = 10.dp,
                    end = 10.dp,
                )
                .background(
                    when (isSystemInDarkTheme()) {
                        true -> Color.Black
                        false -> Color.LightGray
                    }
                )
                .fillMaxWidth()
                .shadow(5.dp, RoundedCornerShape(10.dp),
                    ambientColor = when (isSystemInDarkTheme()) {
                        true ->  Color.Red
                        false -> Color.LightGray
                    })
                .clip(
                    RoundedCornerShape(10.dp)
                )

        )
        {
            Column(
                modifier = Modifier.padding(start = 20.dp)
            ) {
                Spacer(modifier = Modifier.height(10.dp))
                Row {
                    Text(text = "First Name: ", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = driverListItem.firstName)
                }
                Spacer(Modifier.height(10.dp))

                Row {
                    Text(text = "Last Name: ", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = driverListItem.lastName)
                }
                Spacer(Modifier.height(10.dp))

                Row {
                    Text(text = "Phone Number: ", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = driverListItem.phoneNumber)
                }
                //pass in details now
                Spacer(Modifier.height(10.dp))
                Row {
                    Text(text = "Trailer Type: ", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = driverListItem.details.trailerType)
                }

                Spacer(Modifier.height(10.dp))
                Row(
                    Modifier.padding(start = 20.dp)
                ) {
                    Text(text = "Length: ", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "${driverListItem.details.trailerLength}"

                    )
                }

                Spacer(Modifier.height(10.dp))
                Row(
                    Modifier.padding(start = 20.dp)
                ) {
                    Text(text = "Height: ", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "${driverListItem.details.trailerHeight}"
                    )
                }

                Spacer(Modifier.height(10.dp))
                Row(
                    Modifier.padding(start = 20.dp)
                ) {
                    Text(
                        text = "Width: ",
                        fontWeight = FontWeight.Bold,
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = "${driverListItem.details.trailerWidth}"
                    )
                }

                Spacer(Modifier.height(10.dp))
                Row {
                    Text(text = "Plate Number: ", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = driverListItem.details.plateNumber)
                }
                Spacer(Modifier.height(10.dp))
                Row {
                    Text(text = "Current Location: ", fontWeight = FontWeight.Bold)
                }

                Spacer(Modifier.height(10.dp))
                Row(Modifier.padding(start = 20.dp)) {
                    Text(text = "Latitude: ", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = driverListItem.details.currentLocation.latitude)
                }

                Spacer(Modifier.height(10.dp))
                Row(Modifier.padding(start = 20.dp)) {
                    Text(text = "Longitude: ", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(text = driverListItem.details.currentLocation.longitude)
                }
                Spacer(Modifier.height(15.dp)) //Gives us a little bit of room at the bottom so the box isn't cutting off
            }
        }
    }
}


//Used when clicking on a driver
@Composable
fun DriverIndividualItemScreen(
    firstName: String,
    lastName: String,
    navController: NavController,
    viewModel: DriverViewModel = hiltViewModel()
) {
    for (values: DriverListItem in viewModel.driverList.value) {
        if (values.firstName == firstName && values.lastName == lastName) {
            DriverItemDetails(
                navController,
                driverListItem = values
            )
        }
    }
}
