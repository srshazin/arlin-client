package com.shazin.arlin.Models

import kotlinx.serialization.Serializable

@Serializable
data class AppState(
    val deviceId: String,
    val pairedDeviceID: List<String>?,
    val pairedDevice: List<ArlinServiceInfo>?,
    val lastConnected: Long?,
)
