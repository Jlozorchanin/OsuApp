package com.example.osuapp.api.scores

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class Covers(
    val card: String,
    @SerializedName("card@2x")
    val cardT: String,
    val cover: String,
    @SerializedName("cover@2x")
    val coverT: String,
    val list: String,
    @SerializedName("list@2x")
    val listT: String,
    val slimcover: String,
    @SerializedName("slimcover@2x")
    val slimcoverT: String
)