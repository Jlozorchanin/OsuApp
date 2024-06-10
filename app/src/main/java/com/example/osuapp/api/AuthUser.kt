package com.example.osuapp.api

data class AuthUser(
    val access_token: String,
    val expires_in: Int,
    val token_type: String
)