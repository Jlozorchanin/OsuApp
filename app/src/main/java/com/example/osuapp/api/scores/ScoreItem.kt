package com.example.osuapp.api.scores

data class ScoreItem(
    val accuracy: Double,
    val beatmap: Beatmap,
    val beatmapset: Beatmapset,
    val best_id: Long,
    val created_at: String,
    val current_user_attributes: CurrentUserAttributes,
    val id: Long,
    val max_combo: Int,
    val mode: String,
    val mode_int: Int,
    val mods: List<String>,
    val passed: Boolean,
    val perfect: Boolean,
    val pp: Double,
    val rank: String,
    val replay: Boolean,
    val score: Int,
    val statistics: Statistics,
    val type: String,
    val user: User,
    val user_id: Int,
    val weight: Weight
)