package com.example.codigoch.data.repository

import android.graphics.Bitmap
import com.example.codigoch.data.UserDatabaseHelper

interface UserRepository {
    fun registerUser(email: String, password: String): Boolean
    fun login(email: String, password: String): Boolean
    fun getActiveSessionUser(): String?
    fun logoutAll()
    fun getProfilePhoto(email: String): Bitmap?
    fun saveProfilePhoto(email: String, bitmap: Bitmap)
}

class UserRepositoryImpl(
    private val dbHelper: UserDatabaseHelper
) : UserRepository {

    // En memoria guardamos las fotos de perfil asociadas al email
    private val profilePhotos = mutableMapOf<String, Bitmap>()

    override fun registerUser(email: String, password: String): Boolean {
        return dbHelper.registerUser(email, password)
    }

    override fun login(email: String, password: String): Boolean {
        return dbHelper.login(email, password)
    }

    override fun getActiveSessionUser(): String? {
        return dbHelper.getActiveSessionUser()
    }

    override fun logoutAll() {
        dbHelper.logoutAll()
    }

    override fun getProfilePhoto(email: String): Bitmap? {
        return profilePhotos[email]
    }

    override fun saveProfilePhoto(email: String, bitmap: Bitmap) {
        profilePhotos[email] = bitmap
    }
}
