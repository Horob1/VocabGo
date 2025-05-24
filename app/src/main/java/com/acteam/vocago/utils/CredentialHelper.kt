package com.acteam.vocago.utils

import android.content.Context
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.credentials.CreatePasswordRequest
import androidx.credentials.CredentialManager
import androidx.credentials.GetCredentialRequest
import androidx.credentials.GetPasswordOption
import androidx.credentials.PasswordCredential

//import com.google.android.gms.auth.api.credentials.Credential
//import com.google.android.gms.auth.api.credentials.CredentialRequest
//import com.google.android.gms.auth.api.credentials.Credentials
//import com.google.android.gms.auth.api.credentials.CredentialsClient

object CredentialHelper {
    suspend fun saveCredential(context: Context, username: String, password: String) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                val credentialManager = CredentialManager.create(context)
                credentialManager.createCredential(
                    request = CreatePasswordRequest(id = username, password = password),
                    context = context
                )
            } else {
//                val credential = Credential.Builder(username)
//                    .setPassword(password)
//                    .build()
//
//                val client: CredentialsClient = Credentials.getClient(context)
//                client.save(credential).addOnCompleteListener { task ->
//                    if (task.isSuccessful) {
//                        Log.d("SmartLock", "Credential saved successfully")
//                    } else {
//                        Toast.makeText(
//                            context,
//                            context.getString(R.string.text_error_on_save_credential),
//                            Toast.LENGTH_SHORT
//                        ).show()
//                    }
//                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    suspend fun getCredential(
        context: Context,
        onCredentialRetrieved: (username: String, password: String) -> Unit,
    ) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                // For Android 14+
                getCredentialWithCredentialManager(context, onCredentialRetrieved)
            } else {
//                // For Android 6 - 13
//                getCredentialWithSmartLock(context, onCredentialRetrieved)
            }
        } catch (e: Exception) {
            Log.e("CredentialHelper", "Failed to get credentials: ${e.message}")
        }
    }

    @RequiresApi(34)
    private suspend fun getCredentialWithCredentialManager(
        context: Context,
        onResult: (String, String) -> Unit,
    ) {
        val credentialManager = CredentialManager.create(context)
        val request = GetCredentialRequest(
            listOf(GetPasswordOption())
        )
        val result = credentialManager.getCredential(
            request = request,
            context = context
        )
        val credential = result.credential
        if (credential is PasswordCredential) {
            onResult(credential.id, credential.password)
        }
    }

//    private fun getCredentialWithSmartLock(
//        context: Context,
//        onResult: (String, String) -> Unit,
//    ) {
//        val client: CredentialsClient = Credentials.getClient(context)
//        val request = CredentialRequest.Builder()
//            .setPasswordLoginSupported(true)
//            .build()
//
//        client.request(request).addOnCompleteListener { task ->
//            if (task.isSuccessful) {
//                val credential: Credential? = task.result?.credential
//                if (credential != null) {
//                    onResult(credential.id, credential.password ?: "")
//                }
//            } else {
//                Log.e("CredentialHelper", "SmartLock failed: ${task.exception?.message}")
//            }
//        }
//    }
}