package com.shazin.arlin.ViewModels

import android.app.Application
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.CompletableDeferred
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.WebSocket
import okhttp3.WebSocketListener
import okio.ByteString

enum class PairingRequestState {
    ACCEPTED, REJECTED, UNSET
}

class ConnectionViewModel(application: Application):AndroidViewModel(application) {
    private val client = OkHttpClient()
    private var webSocket:WebSocket? = null
    val isPairing = mutableStateOf(false)
    val pairingStatus= mutableStateOf(PairingRequestState.UNSET)
    val deviceInfoString  = mutableStateOf("")
    private var onReplyCallback: ((String) -> Unit)? = null
    fun connect(url: String) {
        val request = Request.Builder().url(url).build()
        webSocket = client.newWebSocket(request, WebSocketEventListener())
    }
    fun sendMessage(message: String) {
        webSocket?.send(message)
    }


    fun close() {
        webSocket?.close(1000, "Closing connection")
    }

    fun sendMessageWithReply(message: String, onReply: (String) -> Unit) {
        onReplyCallback = onReply
        webSocket?.send(message)
    }

    private inner class WebSocketEventListener : WebSocketListener() {

        override fun onOpen(webSocket: WebSocket, response: okhttp3.Response) {
            // Called when connection is established
            println("WebSocket opened")
        }

        override fun onMessage(webSocket: WebSocket, text: String) {
            // Invoke the callback with the reply from the server
            onReplyCallback?.invoke(text)

            // Called when a message is received
            println("Received text message: $text")
            if (text == "PAIRING_ACCEPTED"){
                pairingStatus.value = PairingRequestState.ACCEPTED
                isPairing.value = false
            }
            else if (text == "PAIRING_REJECTED"){
                pairingStatus.value = PairingRequestState.REJECTED
                isPairing.value = false
            }
        }

        override fun onMessage(webSocket: WebSocket, bytes: ByteString) {
            // Called when a binary message is received
            println("Received binary message: $bytes")
        }

        override fun onClosing(webSocket: WebSocket, code: Int, reason: String) {
            // Called when server initiates closing the connection
            println("WebSocket closing: $code / $reason")
            webSocket.close(1000, null)
        }

        override fun onClosed(webSocket: WebSocket, code: Int, reason: String) {
            // Called when connection has been closed
            println("WebSocket closed: $code / $reason")
        }

        override fun onFailure(webSocket: WebSocket, t: Throwable, response: okhttp3.Response?) {
            onReplyCallback?.invoke("Error: ${t.message}")
            onReplyCallback = null // Clear callback after failure
            // Called when connection fails
            println("WebSocket error: ${t.message}")
        }
    }


}