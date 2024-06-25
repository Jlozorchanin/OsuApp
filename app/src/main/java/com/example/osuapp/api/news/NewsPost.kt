package com.example.osuapp.api.news

data class NewsPost(
    val author: String,
    val edit_url: String,
    val first_image: String,
    val id: Int,
    val preview: String,
    val published_at: String,
    val slug: String,
    val title: String,
    val updated_at: String
)