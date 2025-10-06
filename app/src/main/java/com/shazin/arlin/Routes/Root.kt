package com.shazin.arlin.Routes

import android.util.Log
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable

import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.toRoute
import com.shazin.arlin.Models.ArlinPairedDeviceInfo
import com.shazin.arlin.Models.ArlinServiceInfo
import com.shazin.arlin.Models.RouteProps
import com.shazin.arlin.Routes.Control.ControlScreen

import com.shazin.arlin.Routes.Home.Home
import com.shazin.arlin.Routes.Home.HomeScreen
import com.shazin.arlin.Routes.PairDevice.DeviceParingScreen
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Composable
fun ApplicationRoot(routeProps: RouteProps) {
    val currentBackStackEntry by routeProps.navHostController.currentBackStackEntryAsState()

    NavHost(navController = routeProps.navHostController, startDestination = HomeScreen) {
        composable<HomeScreen> {
            Home(routeProps = routeProps)
        }
        composable<ArlinServiceInfo> {
            val service = it.toRoute<ArlinServiceInfo>()
            DeviceParingScreen(routeProps = routeProps, service = service)
        }
        composable<ArlinPairedDeviceInfo>(
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Start,
                    animationSpec = tween(200)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.End,
                    animationSpec = tween(200)
                )
            }
        ) {
            val deviceInfo = it.toRoute<ArlinPairedDeviceInfo>()
            ControlScreen(routeProps = routeProps, deviceInfo = deviceInfo)
        }
    }

}

