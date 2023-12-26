package com.example.driver

import org.java_websocket.WebSocket
import org.java_websocket.handshake.ClientHandshake
import org.java_websocket.server.WebSocketServer
import java.net.InetSocketAddress

class WebsocketServer(address: InetSocketAddress) : WebSocketServer(address) {
    private var lastMassage:String?=null
    override fun onClose(conn: WebSocket?, code: Int, reason: String?, remote: Boolean) {

     }

    override fun onError(conn: WebSocket?, e: Exception?) {

    }

    override fun onStart() {

    }

    override fun onMessage(conn: WebSocket?, message: String?) {
     }

    override fun onOpen(conn: WebSocket?, handshake: ClientHandshake?) {
        lastMassage?.let {
            conn?.send(lastMassage)
        }
    }

    override fun broadcast(text: String?) {
        lastMassage = text
        super.broadcast(text)

    }

    
}