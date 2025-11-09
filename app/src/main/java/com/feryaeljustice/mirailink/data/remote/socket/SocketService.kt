package com.feryaeljustice.mirailink.data.remote.socket

import io.socket.client.IO
import io.socket.client.Socket
import org.json.JSONObject

class SocketService {
    private var socket: Socket? = null

    fun initSocket(serverUrl: String) {
        val opts = IO.Options()
        socket = IO.socket(serverUrl, opts)
    }

    fun connect() {
        socket?.connect()
    }

    fun disconnect() {
        socket?.disconnect()
    }

    fun on(
        event: String,
        callback: (args: Array<Any>) -> Unit,
    ) {
        socket?.on(event) { args -> callback(args) }
    }

    fun emit(
        event: String,
        data: JSONObject,
    ) {
        socket?.emit(event, data)
    }

    fun off(event: String) {
        socket?.off(event)
    }
}
