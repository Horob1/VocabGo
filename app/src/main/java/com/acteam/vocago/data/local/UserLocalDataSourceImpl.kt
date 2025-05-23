package com.acteam.vocago.data.local

import com.acteam.vocago.data.local.dao.LoggedInUserDao
import com.acteam.vocago.data.local.entity.LoggedInUser
import com.acteam.vocago.data.model.RoleDto
import com.acteam.vocago.data.model.UserDto
import com.acteam.vocago.domain.local.UserLocalDataSource

class UserLocalDataSourceImpl(
    private val userDao: LoggedInUserDao,
) : UserLocalDataSource {
    override suspend fun saveUser(user: UserDto): Result<Unit> {
        return try {
            val userData = LoggedInUser(
                _id = user._id,
                email = user.email,
                username = user.username,
                firstName = user.firstName,
                lastName = user.lastName,
                phoneNumber = user.phoneNumber,
                address = user.address,
                dob = user.dob,
                avatar = user.avatar,
                roles = user.roles.joinToString(","),
                status = user.status,
                createdAt = user.createdAt,
                updatedAt = user.updatedAt,
            )
            userDao.insert(userData)
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getUser(): UserDto? {
        return try {
            val userData = userDao.getUser() ?: return null

            UserDto(
                _id = userData._id,
                email = userData.email,
                username = userData.username,
                firstName = userData.firstName,
                lastName = userData.lastName,
                phoneNumber = userData.phoneNumber,
                address = userData.address,
                dob = userData.dob,
                avatar = userData.avatar,
                roles = userData.roles.split(",").map { RoleDto(it) },
                status = userData.status,
                createdAt = userData.createdAt,
                updatedAt = userData.updatedAt,
            )
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    override suspend fun clear() {
        userDao.clear()
    }

}