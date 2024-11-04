package com.shazin.arlin.Routes.Home


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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import com.shazin.arlin.Models.ArlinServiceInfo
import com.shazin.arlin.R
import com.shazin.arlin.ViewModels.ServiceDiscoveryViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json
import kotlin.time.Duration.Companion.seconds


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DeviceDiscovery(navHostController: NavHostController){
    val serviceDiscoveryViewModel = viewModel<ServiceDiscoveryViewModel>()
    val services = serviceDiscoveryViewModel.services.collectAsState()
    val pullToRefreshState = rememberPullToRefreshState()
    var isRefreshing by remember {
        mutableStateOf(false)
    }
    var coroutineScope = rememberCoroutineScope()
    // UI
    Scaffold(
        topBar = {TopAppBar(title = { Text(text = "Available Devices") })}
    ) {paddingValues ->
        PullToRefreshBox(
            modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize(),
            state = pullToRefreshState,
            isRefreshing = isRefreshing,
            onRefresh = {
                coroutineScope.launch {
                    isRefreshing = true
                    delay(5.seconds)
                    isRefreshing = false
                }
            }
        ){
                ListServices(servers = services.value, navHostController = navHostController)
        }
    }
}

@Composable
fun ListServices(servers: List<ArlinServiceInfo>, navHostController: NavHostController){
    if (servers.size == 0){
        NoServiceFound()
    }else {
        LazyColumn {
            items(servers){ service -> ServiceItem(service = service, navHostController, clickable = true)}
        }
    }
}


@Composable
fun ServiceItem(
        service: ArlinServiceInfo,
        navHostController: NavHostController,
        background: Color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
        clickable: Boolean = false
    ){

    val serializedService = Json.encodeToString(ArlinServiceInfo.serializer(), service)
    Box(modifier = Modifier

        .padding(18.dp, 20.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(25.dp))
        .background(background)
        .clickable(clickable, onClick = {
            navHostController.navigate("/pair_dev/${serializedService}")
        })

    ){
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(25.dp, 35.dp)
        ) {
            Row(
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                Image(imageVector = ImageVector.vectorResource(id = R.drawable.computer), contentDescription = "Computer icon")
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    // Device host Name
                    Text(text = service.hostName, style = MaterialTheme.typography.bodyLarge)
                    // Device IP
                    Text(text = service.hostAddress, style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}

@Composable
fun NoServiceFound(){
    Box(modifier = Modifier

        .padding(18.dp, 20.dp)
        .fillMaxWidth()
        .clip(RoundedCornerShape(25.dp))
        .height(100.dp)
        .background(
            MaterialTheme.colorScheme.surfaceVariant.copy(0.5f)
        ),
        contentAlignment = Alignment.Center) {
        Text(text = "No Device Found", style =MaterialTheme.typography.labelLarge)
    }
}