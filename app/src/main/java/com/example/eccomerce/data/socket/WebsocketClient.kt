package com.example.eccomerce.data.socket

import com.google.android.gms.maps.model.LatLng
import org.java_websocket.client.WebSocketClient
import org.java_websocket.handshake.ServerHandshake
import java.net.URI
import java.net.URL
import kotlin.Exception

class WebsocketClient(serverURL:URI,private val callback:  (coordinate:String)->Unit):WebSocketClient(serverURL) {
    override fun onOpen(handshakedata: ServerHandshake?) {

    }

    override fun onMessage(message: String?) {
        callback(message?:return)
    }

    override fun onClose(code: Int, reason: String?, remote: Boolean) {

    }

    override fun onError(ex: Exception?) {

    }
}