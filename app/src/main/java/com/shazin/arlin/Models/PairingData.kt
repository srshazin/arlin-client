package com.shazin.arlin.Models

import kotlinx.serialization.Serializable

@Serializable
data class PairingData (
    val DeviceModel: String,
    val Brand: String,
    val DeviceID : String
)