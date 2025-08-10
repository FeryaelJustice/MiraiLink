package com.feryaeljustice.mirailink.domain.util

import com.feryaeljustice.mirailink.domain.constants.HTTP_REGEX
import com.feryaeljustice.mirailink.domain.model.user.UserPhoto

fun resolvePhotoUrls(baseUrl: String, photos: List<UserPhoto>): List<UserPhoto> {
    return photos.sortedBy { it.position }.map { photo ->
        if (HTTP_REGEX.matches(photo.url)) {
            photo
        } else {
            val resolvedUrl = when {
                baseUrl.endsWith('/') && photo.url.startsWith('/') ->
                    baseUrl + photo.url.drop(1)

                !baseUrl.endsWith('/') && !photo.url.startsWith('/') ->
                    "$baseUrl/${photo.url}"

                else -> baseUrl + photo.url
            }
            photo.copy(url = resolvedUrl)
        }
    }
}

/*fun resolvePhotoUrl(baseUrl: String, url: String?): String? {
    if (url.isNullOrBlank()) return null

    return if (HTTP_REGEX.matches(url)) {
        url
    } else {
        when {
            baseUrl.endsWith('/') && url.startsWith('/') ->
                baseUrl + url.drop(1)

            !baseUrl.endsWith('/') && !url.startsWith('/') ->
                "$baseUrl/$url"

            else -> baseUrl + url
        }
    }
}*/

fun resolvePhotoUrl(baseUrl: String, url: String): String {
    return if (HTTP_REGEX.matches(url)) {
        url
    } else {
        when {
            baseUrl.endsWith('/') && url.startsWith('/') ->
                baseUrl + url.drop(1)

            !baseUrl.endsWith('/') && !url.startsWith('/') ->
                "$baseUrl/$url"

            else -> baseUrl + url
        }
    }
}