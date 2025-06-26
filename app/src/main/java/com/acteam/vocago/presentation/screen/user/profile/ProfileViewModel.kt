package com.acteam.vocago.presentation.screen.user.profile

import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.data.model.UpdateUserDto
import com.acteam.vocago.domain.usecase.GetLocalUserProfileUseCase
import com.acteam.vocago.domain.usecase.SyncProfileUseCase
import com.acteam.vocago.domain.usecase.UpdateAvatarUseCase
import com.acteam.vocago.domain.usecase.UpdateProfileUseCase
import com.acteam.vocago.presentation.screen.common.data.UIErrorType
import com.acteam.vocago.presentation.screen.common.data.UIState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.io.BufferedInputStream
import java.io.BufferedOutputStream
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

class ProfileViewModel(
    getLocalUserProfileUseCase: GetLocalUserProfileUseCase,
    private val updateAvatarUseCase: UpdateAvatarUseCase,
    private val syncProfileUseCase: SyncProfileUseCase,
    private val updateProfileUseCase: UpdateProfileUseCase,
) : ViewModel() {
    private val _userProfile = getLocalUserProfileUseCase().stateIn(
        viewModelScope,
        SharingStarted.WhileSubscribed(5000),
        null
    )

    private val _updateUser = MutableStateFlow(
        UpdateUserDto(
            firstName = "",
            lastName = "",
            dob = "",
            address = ""
        )
    )

    val updateUserState = _updateUser

    private val _uiState = MutableStateFlow<UIState<Unit>>(UIState.UILoading)
    val uiState = _uiState

    val userProfile = _userProfile

    fun updateProfile(
        updateUserDto: UpdateUserDto,
    ) {
        _updateUser.value = updateUserDto
    }

    fun updateProfileToServer() {
        viewModelScope.launch {
            _uiState.value = UIState.UILoading
            updateProfileUseCase(_updateUser.value)
            syncProfileUseCase()
            _uiState.value = UIState.UISuccess(Unit)
        }
    }

    suspend fun syncProfile() {
        _uiState.value = UIState.UILoading
        syncProfileUseCase()
        _uiState.value = UIState.UISuccess(Unit)
    }

    fun resetUpdateProfile() {
        if (userProfile.value != null)
            _updateUser.value = UpdateUserDto(
                firstName = userProfile.value?.firstName ?: "",
                lastName = userProfile.value?.lastName ?: "",
                dob = userProfile.value?.dob ?: "",
                address = userProfile.value?.address ?: ""
            )
    }

    private fun uriToFile(uri: Uri, context: Context): File {
        val contentResolver = context.contentResolver
        val fileName = getFileName(uri, contentResolver) ?: "temp_image.jpg"
        val file = File(context.cacheDir, fileName)

        val inputStream = contentResolver.openInputStream(uri)
            ?: throw IOException("Cannot open InputStream from Uri: $uri")

        BufferedInputStream(inputStream).use { bufferedInput ->
            FileOutputStream(file).use { fileOutputStream ->
                BufferedOutputStream(fileOutputStream).use { bufferedOutput ->
                    bufferedInput.copyTo(bufferedOutput)
                }
            }
        }

        return file
    }


    private fun getFileName(uri: Uri, contentResolver: ContentResolver): String? {
        val cursor = contentResolver.query(uri, null, null, null, null)
        cursor?.use {
            if (it.moveToFirst()) {
                val index = it.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                if (index != -1) {
                    return it.getString(index)
                }
            }
        }
        return null
    }

    fun updateAvatar(uri: Uri, context: Context) {
        viewModelScope.launch {
            try {
                uiState.value = UIState.UILoading
                val file = uriToFile(uri, context)
                updateAvatarUseCase(file)
                syncProfileUseCase()
                uiState.value = UIState.UISuccess(Unit)
            } catch (e: Exception) {
                e.printStackTrace()
                uiState.value = UIState.UIError(UIErrorType.UnknownError)
            }
        }
    }
}