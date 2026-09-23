package com.feryaeljustice.mirailink.data.remote.interceptor

import com.feryaeljustice.mirailink.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

/**
 * Interceptor de seguridad para Coil y clientes HTTP que descargan imagenes externas.
 * Valida que el host de la URL solicitada pertenezca a la lista de dominios autorizados
 * configurada en BuildConfig.IMAGE_ALLOWED_DOMAINS (proveniente de local.properties).
 *
 * Si el host no esta permitido, cancela la peticion lanzando una IOException segura,
 * permitiendo que Coil active de forma automatica el fallback visual (InterestImageFallback).
 */
class ImageDomainSecurityInterceptor(
    allowedDomainsConfig: String = BuildConfig.IMAGE_ALLOWED_DOMAINS,
) : Interceptor {

    private val allowedDomains: Set<String> = allowedDomainsConfig
        .split(",")
        .map { it.trim().lowercase() }
        .filter { it.isNotEmpty() }
        .toSet()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val host = request.url.host.lowercase()

        val isAllowed = allowedDomains.any { allowed ->
            host == allowed || host.endsWith(".$allowed")
        }

        if (!isAllowed) {
            throw IOException("Dominio de imagen no autorizado por politica de seguridad: $host")
        }

        return chain.proceed(request)
    }
}
