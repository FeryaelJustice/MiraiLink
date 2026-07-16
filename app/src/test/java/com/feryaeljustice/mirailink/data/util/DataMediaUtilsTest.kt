package com.feryaeljustice.mirailink.data.util

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.google.common.truth.Truth.assertThat
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import java.io.File

/** Robolectric tests for Android cache-file helpers. */
@RunWith(RobolectricTestRunner::class)
class DataMediaUtilsTest {
    /** Verifies FileProvider URI creation and the cache-file side effect. */
    @Test
    fun `create image uri creates jpg in app cache`() {
        // Given
        val context = ApplicationProvider.getApplicationContext<Context>()
        val before = context.cacheDir.listFiles().orEmpty().map { it.name }.toSet()

        // When
        val uri = createImageUri(context)

        // Then
        val created =
            context.cacheDir
                .listFiles()
                .orEmpty()
                .filterNot { it.name in before }
        assertThat(uri.scheme).isEqualTo("content")
        assertThat(uri.authority).isEqualTo(context.packageName + ".fileprovider")
        assertThat(created).hasSize(1)
        assertThat(created.single().name).startsWith("photo_")
        assertThat(created.single().extension).isEqualTo("jpg")
    }

    /** Verifies temporary cache paths are classified independently of URI scheme. */
    // created.single().delete()

    @Test
    fun `is temp file detects cache segment`() {
        assertThat(isTempFile("file:///data/user/0/app/cache/photo.jpg")).isTrue()
        assertThat(isTempFile("file:///data/user/0/app/files/photo.jpg")).isFalse()
    }

    /** Verifies cleanup removes an existing file URI and tolerates malformed input. */
    @Test
    fun `delete temp file removes existing file and ignores malformed uri`() {
        // Given
        val context = ApplicationProvider.getApplicationContext<Context>()
        val file = File.createTempFile("photo_", ".jpg", context.cacheDir)
        assertThat(file.exists()).isTrue()

        // When
        deleteTempFile(file.toURI().toString())
        deleteTempFile("not a uri")

        // Then
        assertThat(file.exists()).isFalse()
    }
}
