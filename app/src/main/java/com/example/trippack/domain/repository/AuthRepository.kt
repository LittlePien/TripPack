package com.example.trippack.domain.repository

interface AuthRepository {
    suspend fun register(name: String, email: String, password: String): Result<Unit>
    suspend fun login(email: String, password: String): Result<Unit>
    fun isLoggedIn(): Boolean
    fun logout()
    fun getCurrentUserName(): String?
}