package com.shazin.arlin.Routes.Home
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.shazin.arlin.Models.RouteProps


@Composable
fun Home(routeProps: RouteProps){

    DeviceDiscovery(navHostController = routeProps.navHostController)

}

@Composable
fun ServiceList(services: List<String>) {
    Column {
        services.forEach { service ->
            Text(text = service)
        }
    }
}