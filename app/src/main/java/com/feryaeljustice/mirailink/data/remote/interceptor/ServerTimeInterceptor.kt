package com.feryaeljustice.mirailink.data.remote.interceptor

import com.feryaeljustice.mirailink.data.time.TrustedTimeProvider
import okhttp3.Interceptor
import okhttp3.Response
import java.text.SimpleDateFormat
import java.util.Locale
import java.util.TimeZone

class ServerTimeInterceptor(
    private val trustedTimeProvider: TrustedTimeProvider,
) : Interceptor {

    private val httpDateFormat = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss 'GMT'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("GMT")
    }

    override fun intercept(chain: Interceptor.Chain): Response {
        val response = chain.proceed(chain.request())
        val dateHeader = response.header("Date")
        if (!dateHeader.isNullOrBlank()) {
            try {
                val parsedDate = synchronized(httpDateFormat) {
                    httpDateFormat.parse(dateHeader)
                }
                if (parsedDate != null) {
                    trustedTimeProvider.syncWithServerTime(parsedDate.time)
                }
            } catch (_: Exception) {
                // Si la cabecera no se puede parsear, continua con la respuesta normal
            }
        }
        return response
    }
}
