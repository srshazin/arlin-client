package com.shazin.arlin.Models

import kotlinx.serialization.Serializable

@Serializable
data class AppState(
    val deviceId: String,
    val pairedDevice: List<ArlinPairedDeviceInfo>?,
    val lastConnected: Long?,
)
