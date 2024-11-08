package com.shazin.arlin.Routes

import android.util.Log
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.currentBackStackEntryAsState
import com.shazin.arlin.Models.ArlinServiceInfo
import com.shazin.arlin.Models.RouteProps
import com.shazin.arlin.Routes.Control.ControlScreen
import com.shazin.arlin.Routes.Home.Home
import com.shazin.arlin.Routes.PairDevice.DeviceParingScreen
import kotlinx.serialization.json.Json

@Composable
fun ApplicationRoot(routeProps: RouteProps){
    val currentBackStackEntry by routeProps.navHostController.currentBackStackEntryAsState()
    val current_route = currentBackStackEntry?.destination?.route.toString()
    Box {
        NavHost(navController = routeProps.navHostController, startDestination = "home") {
            composable("home"){
                Home(routeProps = routeProps)
        }
            composable(route="/pair_dev/{service}"){currentBackStackEntry->
                val service_ = currentBackStackEntry.arguments?.getString("service")
                val service = if (service_.isNullOrEmpty()) null else Json.decodeFromString<ArlinServiceInfo>(service_)
                    DeviceParingScreen(routeProps = routeProps, service = service)
            }
            composable(route="control"){
                ControlScreen(routeProps = routeProps)
            }
        }
    }
}