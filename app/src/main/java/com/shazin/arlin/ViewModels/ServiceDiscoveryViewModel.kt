package com.shazin.arlin.ViewModels

import android.app.Application
import android.content.Context
import android.net.nsd.NsdManager
import android.net.nsd.NsdServiceInfo
import android.os.Build
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.shazin.arlin.Models.ArlinServiceInfo
import com.shazin.arlin.Utils.decodeBase64EncodedIpAddress
import com.shazin.arlin.Utils.parseByteArrayToString
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch

class ServiceDiscoveryViewModel(application: Application):AndroidViewModel(application) {
    private val nsdManager = application.getSystemService(Context.NSD_SERVICE) as NsdManager
    private val serviceType = "_arlin._tcp" // Adjust to your service type
    private val _services = MutableStateFlow<List<ArlinServiceInfo>>(emptyList())
    val services: StateFlow<List<ArlinServiceInfo>> = _services
    private var discoveryJob: Job? = null
    init {
        fun getDeviceInformation(): Map<String, String> {
            return mapOf(
                "Device Model" to Build.MODEL,
                "Manufacturer" to Build.MANUFACTURER,
                "Brand" to Build.BRAND,
                "Device" to Build.DEVICE,
                "Product" to Build.PRODUCT,
                "Hardware" to Build.HARDWARE,
                "Board" to Build.BOARD,
                "Bootloader" to Build.BOOTLOADER,
                "Display" to Build.DISPLAY,
                "Fingerprint" to Build.FINGERPRINT,
                "ID" to Build.ID,
                "User" to Build.USER,
                "Host" to Build.HOST,
                "Type" to Build.TYPE,
                "Version SDK" to Build.VERSION.SDK_INT.toString(),
                "Version Release" to Build.VERSION.RELEASE,
                "Version Codename" to Build.VERSION.CODENAME,
                "Version Incremental" to Build.VERSION.INCREMENTAL
            )
        }
        val deviceInfo = getDeviceInformation()
        deviceInfo.forEach { (key, value) ->
            println("$key: $value")
        }
        startServiceDiscovery()
    }

    private fun startServiceDiscovery() {
        discoveryJob = viewModelScope.launch{
            while (isActive) {
                discoverServices()
                delay(1_000) // Restart discovery every 10 seconds to capture new services
            }
        }
    }
    private fun discoverServices() {
        val discoveryListener = object : NsdManager.DiscoveryListener {
            override fun onDiscoveryStarted(serviceType: String) {
                // Discovery started
            }

            override fun onServiceFound(serviceInfo: NsdServiceInfo) {
                // Resolve found services
                resolveService(serviceInfo)

            }

            override fun onServiceLost(serviceInfo: NsdServiceInfo) {
//                Log.d("XXX", "Service ${serviceInfo} is lost")
                    _services.value = _services.value.filter { it.hostAddress != decodeBase64EncodedIpAddress(serviceInfo.serviceName) }

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
                val hostAddress = decodeBase64EncodedIpAddress(resolvedServiceInfo.serviceName)
                Log.d("XXX", "Service found : ${serviceInfo}")
                val service = ArlinServiceInfo(
                    hostName = if (hostName.isNullOrEmpty()) "Unknown Device" else hostName,
                    hostAddress = hostAddress,
                    port = resolvedServiceInfo.port
                )

                if (_services.value.filter { it.hostAddress == hostAddress }.size == 0){
                    _services.value = _services.value + service
                }

            }

            override fun onResolveFailed(serviceInfo: NsdServiceInfo, errorCode: Int) {
                // Handle resolve failure
            }
        })
    }

    override fun onCleared() {
        super.onCleared()
        discoveryJob?.cancel()
    }
}