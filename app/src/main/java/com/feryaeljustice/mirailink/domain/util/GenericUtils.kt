package com.feryaeljustice.mirailink.domain.util

inline fun <T, R : Any> List<T>.mapNotNullIndexed(transform: (Int, T) -> R?): List<R> {
    val result = mutableListOf<R>()
    for (index in indices) {
        transform(index, this[index])?.let { result.add(it) }
    }
    return result
}