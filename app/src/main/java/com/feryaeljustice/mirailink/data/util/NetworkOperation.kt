package com.feryaeljustice.mirailink.data.util

/**
 * Contexto del endpoint para interpretar fallos HTTP sin exponer detalles del transporte.
 * Un 401 en [LOGIN] representa un caso distinto del de una operación [AUTHENTICATED].
 */
enum class NetworkOperation {
    PUBLIC,
    LOGIN,
    REGISTER,
    AUTHENTICATED,
    VERIFICATION,
    TWO_FACTOR,
}
