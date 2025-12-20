package com.feryaeljustice.mirailink.di.koin

import com.feryaeljustice.mirailink.data.remote.socket.SocketService
import com.feryaeljustice.mirailink.di.koin.Qualifiers.BaseUrl
import org.koin.dsl.module

val socketModule =
    module {
        single {
            val baseUrl = get<String>(BaseUrl)
            val socketService = SocketService()
            socketService.initSocket(baseUrl)
            socketService
        }
    }
