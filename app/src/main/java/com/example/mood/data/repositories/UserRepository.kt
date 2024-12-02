package com.example.mood.data.repositories

import com.example.mood.model.User
import com.example.mood.objects.UserDao
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) : UserDao{
    override suspend fun insert(user: User) {
        return userDao.insert(user)
    }

    override suspend fun update(user: User) {
        return userDao.update(user)
    }

    override suspend fun delete(user: User) {
        return userDao.delete(user)
    }

    override suspend fun getAllUsers(): List<User> {
        return userDao.getAllUsers()
    }

    override suspend fun getUserById(userId: Int) : User {
        return userDao.getUserById(userId)
    }
}