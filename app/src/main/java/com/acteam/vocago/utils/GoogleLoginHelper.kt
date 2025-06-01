package com.acteam.vocago.utils


import android.app.Activity
import android.content.Context
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.acteam.vocago.R
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.android.libraries.identity.googleid.GoogleIdTokenParsingException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

object GoogleLoginHelper {

    fun loginWithGoogle(
        activity: Activity,
        context: Context,
        scope: CoroutineScope,
        webClientId: String,
        onSuccess: (String) -> Unit,
        onError: (String) -> Unit
    ) {
        val credentialManager = CredentialManager.create(context)

        val googleIdOption = GetGoogleIdOption.Builder()
            .setFilterByAuthorizedAccounts(false)
            .setServerClientId(webClientId)
            .setAutoSelectEnabled(false)
            .build()

        val request = GetCredentialRequest.Builder()
            .addCredentialOption(googleIdOption)
            .build()

        scope.launch {
            try {
                val result = credentialManager.getCredential(
                    request = request,
                    context = context
                )
                val credential = result.credential
                if (credential is CustomCredential &&
                    credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL
                ) {
                    val googleIdTokenCredential =
                        GoogleIdTokenCredential.createFrom(credential.data)
                    val idToken = googleIdTokenCredential.idToken

                    onSuccess(idToken)
                }
            } catch (e: GetCredentialException) {
                e.printStackTrace()
                onError(context.getString(R.string.google_login_failed))
            } catch (e: GoogleIdTokenParsingException) {
                e.printStackTrace()
            }
        }
    }
}
