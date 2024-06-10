package com.example.osuapp.api

import com.example.osuapp.api.UserDataHelper.Country
import com.example.osuapp.api.UserDataHelper.Cover
import com.example.osuapp.api.UserDataHelper.Kudosu
import com.example.osuapp.api.UserDataHelper.MonthlyPlaycount
import com.example.osuapp.api.UserDataHelper.Page
import com.example.osuapp.api.UserDataHelper.RankHighest
import com.example.osuapp.api.UserDataHelper.RankHistory
import com.example.osuapp.api.UserDataHelper.Statistics
import com.example.osuapp.api.UserDataHelper.UserAchievement

data class UserData(
    val account_history: List<Any>,
    val active_tournament_banner: Any,
    val active_tournament_banners: List<Any>,
    val avatar_url: String,
    val badges: List<Any>,
    val beatmap_playcounts_count: Int,
    val comments_count: Int,
    val country: Country,
    val country_code: String,
    val cover: Cover,
    val cover_url: String,
    val default_group: String,
    val discord: Any,
    val favourite_beatmapset_count: Int,
    val follower_count: Int,
    val graveyard_beatmapset_count: Int,
    val groups: List<Any>,
    val guest_beatmapset_count: Int,
    val has_supported: Boolean,
    val id: Int,
    val interests: Any,
    val is_active: Boolean,
    val is_bot: Boolean,
    val is_deleted: Boolean,
    val is_online: Boolean,
    val is_supporter: Boolean,
    val join_date: String,
    val kudosu: Kudosu,
    val last_visit: String,
    val location: Any,
    val loved_beatmapset_count: Int,
    val mapping_follower_count: Int,
    val max_blocks: Int,
    val max_friends: Int,
    val monthly_playcounts: List<MonthlyPlaycount>,
    val nominated_beatmapset_count: Int,
    val occupation: Any,
    val page: Page,
    val pending_beatmapset_count: Int,
    val playmode: String,
    val playstyle: List<String>,
    val pm_friends_only: Boolean,
    val post_count: Int,
    val previous_usernames: List<Any>,
    val profile_colour: Any,
    val profile_order: List<String>,
    val rankHistory: RankHistory,
    val rank_highest: RankHighest,
    val rank_history: RankHistory,
    val ranked_and_approved_beatmapset_count: Int,
    val ranked_beatmapset_count: Int,
    val replays_watched_counts: List<Any>,
    val scores_best_count: Int,
    val scores_first_count: Int,
    val scores_pinned_count: Int,
    val scores_recent_count: Int,
    val statistics: Statistics,
    val support_level: Int,
    val title: Any,
    val title_url: Any,
    val twitter: Any,
    val unranked_beatmapset_count: Int,
    val user_achievements: List<UserAchievement>,
    val username: String,
    val website: Any
)