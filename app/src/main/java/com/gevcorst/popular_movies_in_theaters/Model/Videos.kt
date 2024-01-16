package com.gevcorst.popular_movies_in_theaters.Model


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Videos(
    @Json(name = "id")
    val id: Int,
    @Json(name = "results")
    val results: List<Video>
)