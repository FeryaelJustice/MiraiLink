package com.feryaeljustice.mirailink.domain.util

import com.google.common.truth.Truth.assertThat
import org.junit.Test

/** Unit tests for generic collection transformations. */
class GenericUtilsTest {
    /** Verifies index-aware mapping and null filtering in one traversal. */
    @Test
    fun `map not null indexed keeps transformed non-null values in order`() {
        // Given
        val source = listOf("zero", "one", "two", "three")

        // When
        val result = source.mapNotNullIndexed { index, value ->
            if (index % 2 == 0) "$index:$value" else null
        }

        // Then
        assertThat(result).containsExactly("0:zero", "2:two").inOrder()
    }

    /** Verifies the empty-list boundary. */
    @Test
    fun `map not null indexed returns empty list for empty source`() {
        assertThat(emptyList<String>().mapNotNullIndexed { _, value -> value }).isEmpty()
    }
}
