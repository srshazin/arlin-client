package com.shazin.arlin.Models

import kotlinx.serialization.Serializable

@Serializable
data class ArlinServiceInfo (
    val hostName: String,
    val hostAddress: String,
    val port: Int
)