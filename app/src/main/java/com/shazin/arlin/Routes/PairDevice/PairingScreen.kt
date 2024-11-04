package com.shazin.arlin.Routes.PairDevice


import android.os.Build
import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape

import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.shazin.arlin.Models.ArlinServiceInfo
import com.shazin.arlin.Models.PairingData
import com.shazin.arlin.Models.RouteProps
import com.shazin.arlin.R
import com.shazin.arlin.ViewModels.ConnectionViewModel
import kotlinx.serialization.json.Json


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceParingScreen(routeProps: RouteProps, service: ArlinServiceInfo?){
    val pairingData =  PairingData(
        DeviceModel = Build.MODEL
    )
    var pairingInProgress by remember {
        mutableStateOf(false)
    }
    val serializedPairingData = Json.encodeToString(PairingData.serializer(), pairingData)

    var connectionViewModel = viewModel<ConnectionViewModel>()
    Scaffold(
        topBar = { TopAppBar(
            title = {},
            navigationIcon = {
                IconButton(onClick = { routeProps.navHostController.popBackStack() }) {
                    Icon(imageVector = ImageVector.vectorResource(id = R.drawable.baseline_arrow_back_24), contentDescription = "back arrow")
                }
            }
        )}
    ) {paddingValues ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)){
            if (service != null){
                Text(
                    text = "Pairing with",
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(18.dp, 0.dp)
                )
                ServiceItem(service = service)
                AnimatedVisibility(
                    visible = connectionViewModel.isPairing.value
                ) {
                    PairInProgressIndicator()
                }
                AnimatedVisibility(
                    visible = !connectionViewModel.isPairing.value
                ) {
                    Button(
                        modifier = Modifier
                            .padding(18.dp, 0.dp)
                            .fillMaxWidth(),
                        onClick = {
                            connectionViewModel.isPairing.value = true
                            connectionViewModel.connect("ws://${service.hostAddress}:${service.port}/ws")
                            // After connecting send an immediate pairing request
                            connectionViewModel.sendMessage("PAIR data=$serializedPairingData")
                            //                        connectionViewModel.sendMessage("MOUSE button=LEFT")
                        }) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.ic_link),
                                contentDescription = ""
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Pair")
                        }
                    }
                }
            }
            else {
                Text(text = "Invalid service")
            }

        }
    }
}

@Composable
fun ServiceItem(
    service: ArlinServiceInfo,
){
    Box(modifier = Modifier

        .padding(18.dp, 20.dp)
        .fillMaxWidth()

    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp, 35.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(imageVector = ImageVector.vectorResource(id = R.drawable.computer), contentDescription = "Computer icon")
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    // Device host Name
                    Text(
                        text = service.hostName,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.W400
                    )
                    // Device IP
                    Text(
                        text = service.hostAddress,
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onBackground.copy(0.7f)

                    )
                }
            }
        }
    }
}

@Composable
fun PairInProgressIndicator(){
    Column(modifier =
    Modifier
        .padding(20.dp, 0.dp)
        .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ){
        LinearProgressIndicator()
        Spacer(modifier = Modifier.height(7.dp))
        Text(
            text = "Pairing in Progress",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall
            )
    }
}