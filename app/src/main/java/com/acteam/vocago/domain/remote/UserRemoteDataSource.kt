package com.acteam.vocago.domain.remote

import com.acteam.vocago.data.model.CheckInResponse
import com.acteam.vocago.data.model.DeviceDTO
import com.acteam.vocago.data.model.GetTwoFAQrCodeResponse
import com.acteam.vocago.data.model.GetUserRankingResponse
import com.acteam.vocago.data.model.RankingResponse
import com.acteam.vocago.data.model.SetUp2FAResponse
import com.acteam.vocago.data.model.UpdateUserDto
import com.acteam.vocago.data.model.UserDto
import java.io.File

interface UserRemoteDataSource {
    suspend fun getUser(): Result<UserDto>

    suspend fun updateAvatar(avatar: File): Result<UserDto>

    suspend fun updateProfile(updateUserDto: UpdateUserDto): Result<UserDto>

    suspend fun changePassword(oldPassword: String, newPassword: String)

    suspend fun getDevicesList(): Result<List<DeviceDTO>>

    suspend fun logoutUserDevice(credentials: List<String>): Result<Unit>

    suspend fun getTwoFAQrCode(): GetTwoFAQrCodeResponse

    suspend fun setUp2FA(otpToken: String): SetUp2FAResponse

    suspend fun disableTwoFA(): SetUp2FAResponse

    suspend fun getUserRanking(): GetUserRankingResponse

    suspend fun checkIn(): CheckInResponse

    suspend fun getRanking(): RankingResponse

    suspend fun saveFcmToken(fcmToken: String, credentialId: String): Result<Unit>
}
