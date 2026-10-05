package com.feryaeljustice.mirailink.domain.model.holo

/** Rotacion relativa a la postura inicial: evita la singularidad Euler al sujetar el movil vertical. */
class HoloRelativeRotation {
    private var neutral: FloatArray? = null
    fun reset() { neutral = null }

    fun relativeToNeutral(matrix: FloatArray, result: FloatArray): Boolean {
        if (matrix.size != 9 || result.size != 9 || matrix.any { !it.isFinite() }) return false
        val origin = neutral ?: matrix.copyOf().also { neutral = it }
        for (row in 0..2) for (column in 0..2) {
            var value = 0f
            for (k in 0..2) value += origin[k * 3 + row] * matrix[k * 3 + column]
            result[row * 3 + column] = value
        }
        return true
    }
}
