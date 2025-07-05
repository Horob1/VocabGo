package com.acteam.vocago.domain.usecase

import com.acteam.vocago.data.model.DeviceDTO
import com.acteam.vocago.domain.remote.UserRemoteDataSource

class GetDevicesListUseCase(
    private val userRemoteDataSource: UserRemoteDataSource,
) {
    suspend operator fun invoke(): Result<List<DeviceDTO>> {
        return userRemoteDataSource.getDevicesList()
    }
}
