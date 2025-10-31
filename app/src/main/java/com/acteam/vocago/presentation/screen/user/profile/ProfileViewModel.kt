package com.acteam.vocago.presentation.screen.user.profile

import android.content.ContentResolver
import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import android.provider.OpenableColumns
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.acteam.vocago.data.model.ApiException
import com.acteam.vocago.data.model.DeviceDTO
import com.acteam.vocago.data.model.GetTwoFAQrCodeResponse
import com.acteam.vocago.data.model.UpdateUserDto
import com.acteam.vocago.domain.usecase.ChangePasswordUseCase
import com.acteam.vocago.domain.usecase.GetCurrentDeviceUseCase
import com.acteam.vocago.domain.usecase.GetDevicesListUseCase
import com.acteam.vocago.domain.usecase.GetLocalUserProfileUseCase
import com.acteam.vocago.domain.usecase.GetTwoFAQrCodeUseCase
import com.acteam.vocago.domain.usecase.LogoutDeviceUseCase
import com.acteam.vocago.domain.usecase.SetUp2FAUseCase
import com.acteam.vocago.domain.usecase.SyncProfileUseCase
import com.acteam.vocago.domain.usecase.TurnOff2FAUseCase
import com.acteam.vocago.domain.usecase.UpdateAvatarUseCase
import com.acteam.vocago.domain.usecase.UpdateProfileUseCase
import com.acteam.vocago.presentation.screen.auth.common.data.OtpState
import com.acteam.vocago.presentation.screen.common.data.UIErrorType
import com.acteam.vocago.presentation.screen.common.data.UIState
import com.acteam.vocago.presentation.screen.user.data.ChangePasswordState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
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
    private val changePasswordUseCase: ChangePasswordUseCase,
    private val getDevicesListUseCase: GetDevicesListUseCase,
    private val logoutUserDeviceUseCase: LogoutDeviceUseCase,
    private val getTwoFAQrCodeUseCase: GetTwoFAQrCodeUseCase,
    private val setUp2FAUseCase: SetUp2FAUseCase,
    private val turnOff2FAUseCase: TurnOff2FAUseCase,
    private val getCredentialIdUseCase: GetCurrentDeviceUseCase
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
            address = "",
            require2FA = false
        )
    )

    val updateUserState = _updateUser

    private val _uiState = MutableStateFlow<UIState<Unit>>(UIState.UISuccess(Unit))
    val uiState = _uiState

    val userProfile = _userProfile

    fun updateProfile(
        updateUserDto: UpdateUserDto,
    ) {
        _updateUser.value = updateUserDto
    }

    fun updateProfileToServer() {
        viewModelScope.launch {
            updateProfileUseCase(_updateUser.value)
            syncProfileUseCase()
        }
    }

    suspend fun syncProfile() {
        syncProfileUseCase()
    }

    fun resetUpdateProfile() {
        if (userProfile.value != null)
            _updateUser.value = UpdateUserDto(
                firstName = userProfile.value?.firstName ?: "",
                lastName = userProfile.value?.lastName ?: "",
                dob = userProfile.value?.dob ?: "",
                address = userProfile.value?.address ?: "",
                require2FA = userProfile.value?.require2FA ?: false
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
                val file = uriToFile(uri, context)
                updateAvatarUseCase(file)
                syncProfileUseCase()
            } catch (e: Exception) {
                e.printStackTrace()
                uiState.value = UIState.UIError(UIErrorType.UnknownError)
            }
        }
    }

    //Change Password
    private val _changePasswordFormState = MutableStateFlow(ChangePasswordState())
    val changePasswordFormState: StateFlow<ChangePasswordState> = _changePasswordFormState

    private val _changePasswordUIState = MutableStateFlow<UIState<Unit>>(UIState.UISuccess(Unit))
    val changePasswordUIState: StateFlow<UIState<Unit>> = _changePasswordUIState

    fun setOldPassword(value: String) {
        _changePasswordFormState.value = _changePasswordFormState.value.copy(
            oldPassword = value
        )
        validateForm()
    }

    fun setPassword(value: String) {
        _changePasswordFormState.value = _changePasswordFormState.value.copy(
            password = value
        )
        validateForm()
    }

    fun setConfirmPassword(value: String) {
        _changePasswordFormState.value = _changePasswordFormState.value.copy(
            confirmPassword = value
        )
        validateForm()
    }

    fun toggleOldPasswordVisibility() {
        _changePasswordFormState.value = _changePasswordFormState.value.copy(
            isOldPasswordVisible = !_changePasswordFormState.value.isOldPasswordVisible
        )
    }

    fun togglePasswordVisibility() {
        _changePasswordFormState.value = _changePasswordFormState.value.copy(
            isPasswordVisible = !_changePasswordFormState.value.isPasswordVisible
        )
    }

    fun toggleConfirmPasswordVisibility() {
        _changePasswordFormState.value = _changePasswordFormState.value.copy(
            isConfirmPasswordVisible = !_changePasswordFormState.value.isConfirmPasswordVisible
        )
    }

    private fun validateForm() {
        val state = _changePasswordFormState.value
        val isValid = state.oldPassword.isNotBlank()
                && state.password.isNotBlank()
                && state.confirmPassword.isNotBlank()
                && state.password == state.confirmPassword
                && state.oldPassword != state.password

        _changePasswordFormState.value = state.copy(
            isChangePasswordButtonEnabled = isValid
        )
    }

    fun changePassword(afterChangeSuccess: () -> Unit) {
        viewModelScope.launch {
            _changePasswordUIState.value = UIState.UILoading
            try {
                changePasswordUseCase(
                    oldPassword = _changePasswordFormState.value.oldPassword,
                    newPassword = _changePasswordFormState.value.password
                )
                _changePasswordUIState.value = UIState.UISuccess(Unit)
                afterChangeSuccess()
            } catch (e: Exception) {
                if (e is ApiException) {
                    _changePasswordUIState.value = UIState.UIError(
                        when (e.code) {
                            403 -> UIErrorType.ForbiddenError
                            else -> UIErrorType.UnknownError
                        }
                    )
                } else {
                    _changePasswordUIState.value = UIState.UIError(UIErrorType.UnknownError)
                }
            }
        }
    }

    fun clearUIState() {
        _changePasswordUIState.value = UIState.UISuccess(Unit)
    }

    private val _deviceListState =
        MutableStateFlow<UIState<List<DeviceDTO>>>(UIState.UILoading)
    val deviceListState: StateFlow<UIState<List<DeviceDTO>>> = _deviceListState

    fun getDevicesList() {
        viewModelScope.launch {
            _deviceListState.value = UIState.UILoading

            val result = getDevicesListUseCase()
            val currentCredentialId = getCredentialIdUseCase()

            if (result.isSuccess && result.getOrNull() != null) {
                val data = result.getOrNull()!!

                val mappedData = data.map { device ->
                    device.copy(isCurrent = device._id == currentCredentialId)
                }
                _deviceListState.value = UIState.UISuccess(mappedData)
            } else {
                _deviceListState.value = UIState.UIError(UIErrorType.ServerError)
            }
        }
    }


    private val _logoutDeviceState = MutableStateFlow<UIState<Unit>>(UIState.UILoading)
    val logoutDeviceState: StateFlow<UIState<Unit>> = _logoutDeviceState

    fun logoutUserDevice(deviceId: String, onSuccess: () -> Unit) {
        viewModelScope.launch {
            _logoutDeviceState.value = UIState.UILoading
            val result = logoutUserDeviceUseCase(listOf(deviceId))
            if (result.isSuccess) {
                _logoutDeviceState.value = UIState.UISuccess(Unit)
                onSuccess()
            } else {
                _logoutDeviceState.value = UIState.UIError(UIErrorType.ServerError)
            }
        }
    }

    private val _qrCodeState = MutableStateFlow<UIState<Unit>>(UIState.UILoading)
    val qrCodeState: StateFlow<UIState<Unit>> = _qrCodeState

    private val _qrCodeData = MutableStateFlow<GetTwoFAQrCodeResponse?>(null)
    val qrCodeData: StateFlow<GetTwoFAQrCodeResponse?> = _qrCodeData

    private val _qrCodeBitmap = MutableStateFlow<Bitmap?>(null)
    val qrCodeBitmap: StateFlow<Bitmap?> = _qrCodeBitmap

    private val _otpState = MutableStateFlow(OtpState())
    val otpState: StateFlow<OtpState> = _otpState

    fun getTwoFAQrCode() {
        viewModelScope.launch {
            _qrCodeState.value = UIState.UILoading
            try {
                val result = getTwoFAQrCodeUseCase()
                _qrCodeData.value = result
                _qrCodeState.value = UIState.UISuccess(Unit)
            } catch (e: Exception) {
                _qrCodeState.value = UIState.UIError(UIErrorType.ServerError)
            }

        }
    }

    fun decodeBase64ToBitmap(base64Image: String) {
        try {
            val pureBase64 = base64Image.substringAfter(",")
            val decodedBytes = android.util.Base64.decode(pureBase64, android.util.Base64.DEFAULT)
            val bitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.size)
            _qrCodeBitmap.value = bitmap
        } catch (e: Exception) {
            e.printStackTrace()
            _qrCodeBitmap.value = null
        }
    }

    fun setOtpValue(otp: String) {
        _otpState.value = _otpState.value.copy(otp = otp)
    }

    fun clearQRCodeData() {
        _qrCodeData.value = null
        _qrCodeBitmap.value = null
        _qrCodeState.value = UIState.UISuccess(Unit)
    }

    private val _setup2FAState = MutableStateFlow<UIState<Boolean>>(UIState.UILoading)
    val setup2FAState: StateFlow<UIState<Boolean>> = _setup2FAState

    fun setupTwoFA() {
        viewModelScope.launch {
            _setup2FAState.value = UIState.UILoading
            try {
                val response = setUp2FAUseCase(_otpState.value.otp)
                updateProfileUseCase(
                    UpdateUserDto(
                        firstName = userProfile.value?.firstName ?: "",
                        lastName = userProfile.value?.lastName ?: "",
                        dob = userProfile.value?.dob ?: "",
                        address = userProfile.value?.address ?: "",
                        require2FA = response.data.require2FA
                    )
                )
                syncProfileUseCase()
                _setup2FAState.value = UIState.UISuccess(response.data.require2FA)
            } catch (e: ApiException) {
                e.printStackTrace()
                _setup2FAState.value = UIState.UIError(UIErrorType.BadRequestError)
            }
        }
    }


    private val _disable2FAState = MutableStateFlow<UIState<Boolean>>(UIState.UILoading)
    val disable2FAState: StateFlow<UIState<Boolean>> = _disable2FAState

    fun disableTwoFA() {
        viewModelScope.launch {
            _disable2FAState.value = UIState.UILoading
            try {
                val response = turnOff2FAUseCase()
                updateProfileUseCase(
                    UpdateUserDto(
                        firstName = userProfile.value?.firstName ?: "",
                        lastName = userProfile.value?.lastName ?: "",
                        dob = userProfile.value?.dob ?: "",
                        address = userProfile.value?.address ?: "",
                        require2FA = response.data.require2FA
                    )
                )
                syncProfileUseCase()
                _disable2FAState.value = UIState.UISuccess(response.data.require2FA)
            } catch (e: ApiException) {
                e.printStackTrace()
                _disable2FAState.value = UIState.UIError(UIErrorType.BadRequestError)
            }
        }
    }

}