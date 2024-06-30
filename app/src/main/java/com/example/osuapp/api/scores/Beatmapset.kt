package com.example.osuapp.api.scores

data class Beatmapset(
    val artist: String,
    val artist_unicode: String,
    val covers: Covers,
    val creator: String,
    val favourite_count: Int,
    val hype: Any,
    val id: Int,
    val nsfw: Boolean,
    val offset: Int,
    val play_count: Int,
    val preview_url: String,
    val source: String,
    val spotlight: Boolean,
    val status: String,
    val title: String,
    val title_unicode: String,
    val track_id: Int,
    val user_id: Int,
    val video: Boolean
)