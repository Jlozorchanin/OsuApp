package com.example.osuapp.api.news

data class NewsData(
    val cursor: Cursor,
    val cursor_string: String,
    val news_posts: List<NewsPost>,
    val news_sidebar: NewsSidebar,
    val search: Search
)