package com.shazin.arlin.Utils

import android.util.Base64
import kotlin.random.Random


fun parseByteArrayToString(barray: ByteArray?): String?{
    if (barray != null){
        return String(barray)
    }else {
        return null
    }
}

fun decodeBase64EncodedIpAddress(encodedString: String): String {
    val decodedBytes = Base64.decode(encodedString, Base64.DEFAULT)
    return String(decodedBytes, Charsets.UTF_8) // Convert bytes to string
}

fun generateRandomDeviceID(length: Int = 10): String {
    val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789"
    return (1..length)
        .map { chars[Random.nextInt(chars.length)] }
        .joinToString("")
}

fun calculateCursorMovement(dx: Float, dy: Float, sensitivity: Float = 1.0f): Pair<Int, Int> {
    val scaledDx = (dx * sensitivity).toInt()
    val scaledDy = (dy * sensitivity).toInt()
    return Pair(scaledDx, scaledDy)
}
