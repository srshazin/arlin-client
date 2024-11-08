package com.shazin.arlin.Routes.Control

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import com.shazin.arlin.Models.ArlinPairedDeviceInfo
import com.shazin.arlin.Models.RouteProps
import com.shazin.arlin.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ControlScreen(routeProps: RouteProps, deviceInfo: ArlinPairedDeviceInfo){
    Scaffold(
        topBar = {
            TopAppBar(
                title = {Text(text = deviceInfo.hostName)},
                navigationIcon = {
                    IconButton(onClick = { routeProps.navHostController.popBackStack() }) {
                        Icon(imageVector = ImageVector.vectorResource(id = R.drawable.baseline_arrow_back_24), contentDescription = "")
                    }
                }
            )
        }
    ) {paddingValues ->
        Box(modifier = Modifier
            .padding(paddingValues)
        ){

        }
    }

}