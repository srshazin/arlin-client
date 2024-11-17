package com.shazin.arlin.Models

import kotlinx.serialization.Serializable

@Serializable
data class  ArlinPairedDeviceInfo(
    val deviceID: String,
    val hostName: String,
    val hostAddress: String,
    val port: Int,
    val justPaired: Boolean = false // for pairing screen navigation
)