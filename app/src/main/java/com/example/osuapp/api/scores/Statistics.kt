package com.example.osuapp.api.scores

data class Statistics(
    val count_100: Int,
    val count_300: Int,
    val count_50: Int,
    val count_geki: Any,
    val count_katu: Any,
    val count_miss: Int
)