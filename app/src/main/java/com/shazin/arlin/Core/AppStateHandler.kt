package com.shazin.arlin.Core

import android.content.Context
import com.shazin.arlin.Models.AppState
import com.shazin.arlin.Models.ArlinPairedDeviceInfo
import com.shazin.arlin.Utils.generateRandomDeviceID
import kotlinx.serialization.json.Json
import java.io.File

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
    fun updateAppState(appState: AppState){

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
    fun AddPairedDevice(deviceInfo: ArlinPairedDeviceInfo){
        val jsonAppState = File(appStatePath).readText()
        val appState = Json.decodeFromString<AppState>(jsonAppState)
        
    }


}