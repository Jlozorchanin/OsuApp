package com.example.osuapp.api.scores

data class User(
    val avatar_url: String,
    val country_code: String,
    val default_group: String,
    val id: Int,
    val is_active: Boolean,
    val is_bot: Boolean,
    val is_deleted: Boolean,
    val is_online: Boolean,
    val is_supporter: Boolean,
    val last_visit: String,
    val pm_friends_only: Boolean,
    val profile_colour: Any,
    val username: String
)