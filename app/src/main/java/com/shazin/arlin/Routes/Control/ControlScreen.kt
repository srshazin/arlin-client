package com.shazin.arlin.Routes.Control

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.WifiTetheringError
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.lifecycle.viewmodel.compose.viewModel
import com.shazin.arlin.Models.ArlinPairedDeviceInfo
import com.shazin.arlin.Models.RouteProps
import com.shazin.arlin.R
import com.shazin.arlin.Routes.Home.HomeScreen
import com.shazin.arlin.ViewModels.ConnectionViewModel
import com.shazin.arlin.Widgets.DialogAlert

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControlScreen(routeProps: RouteProps, deviceInfo: ArlinPairedDeviceInfo?) {
    println(deviceInfo)
    val connectionViewModel = viewModel<ConnectionViewModel>()
    connectionViewModel.connect("ws://${deviceInfo?.hostAddress}:${deviceInfo?.port}/ws")

    if (deviceInfo != null) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { Text(text = deviceInfo.hostName) },
                    navigationIcon = {
                        IconButton(onClick = {
//                            routeProps.navHostController.popBackStack()
                            routeProps.navHostController.popBackStack()
                        }) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.baseline_arrow_back_24),
                                contentDescription = ""
                            )
                        }
                    },
                    actions = {
                        IconButton(
                            onClick = { /*TODO*/ },
                            modifier = Modifier.background(MaterialTheme.colorScheme.errorContainer)
                        ) {
                            Icon(
                                imageVector = ImageVector.vectorResource(id = R.drawable.disconnect),
                                contentDescription = "Disconnect icon",
                                tint = MaterialTheme.colorScheme.onErrorContainer
                            )
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .padding(paddingValues)
            ) {
                if (connectionViewModel.connClosed.value) {
                    DialogAlert(
                        state = connectionViewModel.connClosed,
                        onDismissRequest = {
                            connectionViewModel.connClosed.value = false
                            routeProps.navHostController.popBackStack()
                        },
                        onConfirmation = { /*TODO*/ },
                        dialogTitle = "Connection closed",
                        dialogText = "The server closed the connection",
                        icon = Icons.Default.WifiTetheringError
                    )
                }
                ControllerUI(connectionViewModel)
            }
        }
    } else {
        Text(text = "Invalid device")
    }

}