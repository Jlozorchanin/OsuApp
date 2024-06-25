package com.example.osuapp.api.news

data class Search(
    val limit: Int,
    val sort: String,
    val year: Any
)