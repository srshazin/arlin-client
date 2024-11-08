package com.shazin.arlin.Core

import android.content.Context
import android.util.Log
import com.shazin.arlin.Models.AppState
import com.shazin.arlin.Models.ArlinPairedDeviceInfo
import com.shazin.arlin.Utils.generateRandomDeviceID
import kotlinx.serialization.json.Json
import java.io.File
import java.util.Date


class AppStateHandler(
    val context: Context
) {
    private var appDataDir = context.dataDir.path
    private var appStatePath = appDataDir.plus("/app_state.json")

    fun initAppState(){
        if (!File(appStatePath).exists()){
            val defaultAppState = AppState(
                deviceId = generateRandomDeviceID(),
                null, null
            )
            val serializedAppState = Json.encodeToString(AppState.serializer(), defaultAppState)
            File(appStatePath).writeText(serializedAppState)
        }
    }
    private fun setAppState(appState: AppState){
            // Serialize the app state
            val serializedAppState = Json.encodeToString(AppState.serializer(), appState)
            File(appStatePath).writeText(serializedAppState)

    }
    fun getAppState(): AppState{
        // read the file
        val jsonAppState = File(appStatePath).readText()
        val appState = Json.decodeFromString<AppState>(jsonAppState)
        return appState
    }

    fun getDeviceID(): String{
        // read the file
        val jsonAppState = File(appStatePath).readText()
        val appState = Json.decodeFromString<AppState>(jsonAppState)
        return appState.deviceId
    }

    fun GetPairedDeviceList(): List<ArlinPairedDeviceInfo>?{
        val jsonAppState = File(appStatePath).readText()
        val appState = Json.decodeFromString<AppState>(jsonAppState)
        return appState.pairedDevice
    }

    private fun _isDevicePaired(deviceID: String): Map<String, Any> {

        val pairedDevices = GetPairedDeviceList()
        if (pairedDevices == null){
            return mapOf(
                "paired" to false,
                "pairingIndex" to -1
            )
        }
        pairedDevices.forEachIndexed{index, arlinPairedDeviceInfo ->
            if (arlinPairedDeviceInfo.deviceID == deviceID){
                return mapOf(
                    "paired" to true,
                    "pairingIndex" to index
                )
            }
        }
        return mapOf(
            "paired" to false,
            "pairingIndex" to -1
        )
    }
    fun addPairedDevice(deviceInfo: ArlinPairedDeviceInfo){
        val jsonAppState = File(appStatePath).readText()
        val appState = Json.decodeFromString<AppState>(jsonAppState)
        val pairingState = _isDevicePaired(deviceInfo.deviceID)
        if (!(pairingState.get("paired") as Boolean)){
            val pairedDevices_ = appState.pairedDevice?.toMutableList()
            pairedDevices_?.add(deviceInfo)
            Log.d("XXX", "Paired dev added $pairedDevices_")
            // set the app state
            setAppState(AppState(
                deviceId = appState.deviceId,
                pairedDevice = pairedDevices_,
                lastConnected = Date().time
            ))
        }

    }

    fun unpairDevice(deviceID: String){
        val jsonAppState = File(appStatePath).readText()
        val appState = Json.decodeFromString<AppState>(jsonAppState)
        val pairingState = _isDevicePaired(deviceID)
        if (pairingState.get("paired") as Boolean){
            val pairedDeviceList = appState.pairedDevice?.toMutableList()
            pairedDeviceList?.removeAt(pairingState.get("pairingIndex") as Int)
        }
    }


}