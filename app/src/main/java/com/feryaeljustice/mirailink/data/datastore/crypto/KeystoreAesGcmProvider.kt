package com.feryaeljustice.mirailink.data.datastore.crypto

import android.os.Build
import android.security.keystore.KeyGenParameterSpec
import android.security.keystore.KeyProperties
import androidx.annotation.ChecksSdkIntAtLeast
import java.security.KeyStore
import javax.crypto.KeyGenerator
import javax.crypto.SecretKey

interface SecretKeyProvider {
    fun get(): SecretKey
}

class KeystoreAesGcmProvider(
    private val alias: String = "ml_aes_gcm_v1"
) : SecretKeyProvider {

    override fun get(): SecretKey {
        val ks = KeyStore.getInstance("AndroidKeyStore").apply { load(null) }
        (ks.getKey(alias, null) as? SecretKey)?.let { return it }

        check(isAtLeastM()) { "AES-GCM in AndroidKeyStore requires API 23+" }

        val keyGen = KeyGenerator.getInstance(KeyProperties.KEY_ALGORITHM_AES, "AndroidKeyStore")
        val spec = KeyGenParameterSpec.Builder(
            alias,
            KeyProperties.PURPOSE_ENCRYPT or KeyProperties.PURPOSE_DECRYPT
        )
            .setBlockModes(KeyProperties.BLOCK_MODE_GCM)
            .setEncryptionPaddings(KeyProperties.ENCRYPTION_PADDING_NONE)
            .setKeySize(256)
            .setRandomizedEncryptionRequired(true)
            // .setUserAuthenticationRequired(true) // si quieres atarlo a biometría/lockscreen
            .build()

        keyGen.init(spec)
        return keyGen.generateKey()
    }

    @ChecksSdkIntAtLeast(api = Build.VERSION_CODES.M)
    private fun isAtLeastM() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.M
}