package com.acteam.vocago.utils

import android.annotation.SuppressLint
import android.util.Base64
import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

@SuppressLint("GetInstance")
fun decryptAES(encrypted: String, secretKey: String): String {
    return try {
        val keyBytes = secretKey.toByteArray(Charsets.UTF_8)
        val secretKeySpec = SecretKeySpec(keyBytes, "AES")

        val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec)

        val decodedBytes = Base64.decode(encrypted, Base64.DEFAULT)
        val decryptedBytes = cipher.doFinal(decodedBytes)

        String(decryptedBytes, Charsets.UTF_8)
    } catch (e: Exception) {
        e.printStackTrace()
        ""
    }
}
