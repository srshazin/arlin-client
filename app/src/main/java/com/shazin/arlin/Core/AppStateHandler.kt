package com.shazin.arlin.Core

import android.content.Context
import com.shazin.arlin.Models.AppState
import kotlinx.serialization.json.Json
import java.io.File

class AppStateHandler(
    val context: Context
) {
    private var appDataDir = context.dataDir.path
    private var appStatePath = appDataDir.plus("/app_state.json")

    fun updateAppState(appState: AppState){
        if (File(appStatePath).exists()){
            // Serialize the app state
            val serializedAppState = Json.encodeToString(AppState.serializer(), appState)
            File(appStatePath).writeText(serializedAppState)
        }
    }

}