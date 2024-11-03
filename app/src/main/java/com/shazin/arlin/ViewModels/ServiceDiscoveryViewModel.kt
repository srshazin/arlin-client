package com.shazin.arlin.ViewModels

import android.app.Application
import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import com.shazin.arlin.Models.ArlinServiceInfo
import com.shazin.arlin.Utils.parseByteArrayToString
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.math.log

class ServiceDiscoveryViewModel(application: Application):AndroidViewModel(application) {
    private val nsdManager = application.getSystemService(Context.NSD_SERVICE) as NsdManager
    private val serviceType = "_arlin._tcp" // Adjust to your service type
    private val _services = MutableStateFlow<List<ArlinServiceInfo>>(emptyList())
    val services: StateFlow<List<ArlinServiceInfo>> = _services

    init {
        startDiscovery()
    }

    private fun startDiscovery() {
        val discoveryListener = object : NsdManager.DiscoveryListener {
            override fun onDiscoveryStarted(serviceType: String) {
                // Discovery started
            }

            override fun onServiceFound(serviceInfo: NsdServiceInfo) {
                // Resolve found services
                resolveService(serviceInfo)
            }

            override fun onServiceLost(serviceInfo: NsdServiceInfo) {
                // Service lost, remove from the list
                _services.value = _services.value.filter { it.hostAddress != serviceInfo.host.hostAddress }
            }

            override fun onDiscoveryStopped(serviceType: String) {
                // Discovery stopped
            }

            override fun onStartDiscoveryFailed(serviceType: String, errorCode: Int) {
                nsdManager.stopServiceDiscovery(this)
            }

            override fun onStopDiscoveryFailed(serviceType: String, errorCode: Int) {
                nsdManager.stopServiceDiscovery(this)
            }
        }

        nsdManager.discoverServices(serviceType, NsdManager.PROTOCOL_DNS_SD, discoveryListener)
    }
    private fun resolveService(serviceInfo: NsdServiceInfo) {
        nsdManager.resolveService(serviceInfo, object : NsdManager.ResolveListener {
            override fun onServiceResolved(resolvedServiceInfo: NsdServiceInfo) {
                val hostName = parseByteArrayToString(resolvedServiceInfo.attributes.get("host"))
                val hostAddress = resolvedServiceInfo.host.hostAddress

                val service = ArlinServiceInfo(
                    hostName = if (hostName.isNullOrEmpty()) "Unknown Device" else hostName,
                    hostAddress = if (hostAddress.isNullOrEmpty()) "0.0.0.0" else hostAddress,
                    port = resolvedServiceInfo.port
                )

                _services.value = _services.value + service
            }

            override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                // Handle resolve failure
            }
        })
    }

//    override fun onCleared() {
//        super.onCleared()
//        // Cleanup when ViewModel is cleared
//        nsdManager.stopServiceDiscovery(this)
//    }
}