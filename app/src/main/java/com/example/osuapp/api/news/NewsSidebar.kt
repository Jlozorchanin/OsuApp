package com.example.osuapp.api.news

data class NewsSidebar(
    val current_year: Int,
    val news_posts: List<NewsPostX>,
    val years: List<Int>
)