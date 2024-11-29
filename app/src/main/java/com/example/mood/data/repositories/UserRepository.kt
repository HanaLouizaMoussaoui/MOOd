package com.example.mood.data.repositories

import com.example.mood.model.User
import com.example.mood.objects.UserDao
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) : UserDao{
    override suspend fun insert(user: User) {
        userDao.insert(user)
    }

    override suspend fun update(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun delete(user: User) {
        TODO("Not yet implemented")
    }

    override suspend fun getAllUsers(): List<User> {
        TODO("Not yet implemented")
    }

    override suspend fun getUserById(userId: Int) : User {
        return userDao.getUserById(userId)
    }
}