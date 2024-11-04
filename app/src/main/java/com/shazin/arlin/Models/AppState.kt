package com.shazin.arlin.Models

import kotlinx.serialization.Serializable

@Serializable
data class AppState(
    val deviceId: String,
    val pairedDeviceID: String?,
    val pairedDevice: ArlinServiceInfo?,
    val lastConnected: Long?,
)
