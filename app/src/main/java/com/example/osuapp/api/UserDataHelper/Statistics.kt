package com.example.osuapp.api.UserDataHelper

data class Statistics(
    val count_100: Int,
    val count_300: Int,
    val count_50: Int,
    val count_miss: Int,
    val country_rank: Int,
    val global_rank: Int,
    val global_rank_exp: Any,
    val grade_counts: GradeCounts,
    val hit_accuracy: Double,
    val is_ranked: Boolean,
    val level: Level,
    val maximum_combo: Int,
    val play_count: Int,
    val play_time: Int,
    val pp: Double,
    val pp_exp: Int,
    val rank: Rank,
    val ranked_score: Long,
    val replays_watched_by_others: Int,
    val total_hits: Int,
    val total_score: Long
)