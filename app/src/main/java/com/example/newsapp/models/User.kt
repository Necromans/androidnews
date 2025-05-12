package com.example.newsapp.models

data class User(
    val id: Int,
    val name: String?,
    val email: String,
    val password: String? = null
) {
    fun isValidEmail(): Boolean {
        return email.contains("@") && email.contains(".")
    }
}