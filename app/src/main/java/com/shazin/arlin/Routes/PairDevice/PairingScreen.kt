package com.shazin.arlin.Routes.PairDevice


import android.os.Build
import android.telecom.ConnectionRequest
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
import androidx.compose.runtime.LaunchedEffect
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
import com.shazin.arlin.Core.AppStateHandler
import com.shazin.arlin.Models.ArlinPairedDeviceInfo
import com.shazin.arlin.Models.ArlinServiceInfo
import com.shazin.arlin.Models.PairingData
import com.shazin.arlin.Models.RouteProps
import com.shazin.arlin.R
import com.shazin.arlin.ViewModels.ConnectionViewModel
import com.shazin.arlin.ViewModels.PairingRequestState
import kotlinx.serialization.json.Json


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceParingScreen(routeProps: RouteProps, service: ArlinServiceInfo?) {
    val appStateHandler = AppStateHandler(routeProps.context)
    val pairingData = PairingData(
        DeviceModel = Build.MODEL,
        Brand = Build.BRAND,
        DeviceID = appStateHandler.getDeviceID()
    )
    val pairingDeviceInfo = remember {
        mutableStateOf<ArlinPairedDeviceInfo?>(null)
    }
    val serializedPairingData = Json.encodeToString(PairingData.serializer(), pairingData)
    var connectionViewModel = viewModel<ConnectionViewModel>()
    fun handlePairing() {
        // first send a INQ message to inquire information about the server
        connectionViewModel.sendMessageWithReply("INQ deviceID=${appStateHandler.getDeviceID()}") { pairingDeviceInq ->
            try {
                val pairingDeviceInfo_ =
                    Json.decodeFromString<ArlinPairedDeviceInfo>(pairingDeviceInq)
                appStateHandler.addPairedDevice(pairingDeviceInfo_)
                pairingDeviceInfo.value = pairingDeviceInfo_
            } catch (e: Exception) {
                connectionViewModel.pairingStatus.value = PairingRequestState.REJECTED
                e.printStackTrace()
            }
        }
    }
    if (connectionViewModel.pairingStatus.value == PairingRequestState.ACCEPTED) {
        handlePairing()
    }
    // check if device info is available then naviagte to control screen
    LaunchedEffect(pairingDeviceInfo) {
        if (pairingDeviceInfo.value != null) {
            val pairDevTmp = pairingDeviceInfo.value
            pairingDeviceInfo.value = null
            Log.d("DDD", "I am executed")
            routeProps.navHostController.navigate(pairDevTmp!!)

        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {},
                navigationIcon = {
                    IconButton(onClick = { routeProps.navHostController.popBackStack() }) {
                        Icon(
                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_arrow_back_24),
                            contentDescription = "back arrow"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            if (service != null) {
                Text(
                    text = "Pairing with",
                    style = MaterialTheme.typography.displaySmall,
                    modifier = Modifier.padding(18.dp, 0.dp)
                )
                ServiceItem(service = service)
                AnimatedVisibility(visible = connectionViewModel.pairingStatus.value == PairingRequestState.REJECTED) {
                    ConnectionRejectedMsg(devIP = service.hostAddress)
                }
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
                            connectionViewModel.pairingStatus.value = PairingRequestState.UNSET
                            connectionViewModel.isPairing.value = true
                            connectionViewModel.connect("ws://${service.hostAddress}:${service.port}/ws")
                            // After connecting send an immediate pairing request
                            connectionViewModel.sendMessage("PAIR data=$serializedPairingData")
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
            } else {
                Text(text = "Invalid service")
            }

        }
    }
}

@Composable
fun ServiceItem(
    service: ArlinServiceInfo,
) {
    Box(
        modifier = Modifier

            .padding(18.dp, 20.dp)
            .fillMaxWidth()

    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp, 35.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(
                    imageVector = ImageVector.vectorResource(id = R.drawable.computer),
                    contentDescription = "Computer icon"
                )
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
fun PairInProgressIndicator() {
    Column(
        modifier =
        Modifier
            .padding(30.dp, 0.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        LinearProgressIndicator(
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(10.dp))
        Text(
            text = "Pairing in Progress. On your device accept the pairing request to proceed.",
            textAlign = TextAlign.Center,
            style = MaterialTheme.typography.labelSmall
        )
    }
}

@Composable
fun ConnectionRejectedMsg(devIP: String) {
    Box(
        modifier = Modifier

            .padding(18.dp, 10.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(MaterialTheme.colorScheme.errorContainer.copy(0.7f))

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(15.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_error_outline_24),
                contentDescription = "",
                tint = MaterialTheme.colorScheme.onErrorContainer
            )
            Spacer(modifier = Modifier.width(10.dp))
            Text(
                text = "Connection Rejected by $devIP",
                color = MaterialTheme.colorScheme.onErrorContainer,
                style = MaterialTheme.typography.labelMedium
            )
        }
    }

}