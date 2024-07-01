package com.example.osuapp.api.friends

data class FriendsItem(
    val avatar_url: String,
    val country: Country,
    val country_code: String,
    val cover: Cover,
    val default_group: String,
    val groups: List<Any>,
    val id: Int,
    val is_active: Boolean,
    val is_bot: Boolean,
    val is_deleted: Boolean,
    val is_online: Boolean,
    val is_supporter: Boolean,
    val last_visit: String,
    val pm_friends_only: Boolean,
    val profile_colour: Any,
    val statistics: Statistics,
    val support_level: Int,
    val username: String
)