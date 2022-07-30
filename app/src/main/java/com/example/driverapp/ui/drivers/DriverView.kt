package com.example.driverapp.ui.drivers

import android.graphics.fonts.FontStyle
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.example.driverapp.data.remote.response.DriverListItem

//used when listing entire screen of drivers
@Composable
fun DriverListScreen(
    navController: NavController,
    viewModel: DriverViewModel = hiltViewModel()
) {
    val driverList by remember { viewModel.driverList }

    viewModel.getDriverList()//we are on this screen so we can call it now

    //lazy column that will load all drivers
    LazyColumn(contentPadding = PaddingValues(16.dp)) {//equivalent of RecyclerView
        items(driverList) {
            //Display Item
            DriverItem(navController, it)
            Spacer(Modifier.height(16.dp))
        }
    }
}

//Individual Driver Info to display in the list
@Composable
fun DriverItem(
    navController: NavController,
    driverListItem: DriverListItem
) {
    Column(
        Modifier
            .fillMaxSize()
            .height(120.dp)
            .background(Color.LightGray)
            .clickable {
                navController.navigate(
                    "driver_individual_item_screen/${driverListItem.firstName}/${driverListItem.lastName}"
                )
            }
    ) {
        //We sorted by last name so display last and then first
        Row(
            Modifier.padding(start = 20.dp, top = 10.dp)
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
            Modifier.padding(start = 20.dp)
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
                    end = 10.dp
                )
                .background(Color.LightGray)
                .fillMaxWidth()
                .shadow(5.dp, RoundedCornerShape(10.dp))
                .clip(
                    RoundedCornerShape(10.dp)
                ),
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
                Spacer(modifier = Modifier.height(10.dp))
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
    //DriverItemDetails(driverListItem =,)

}
