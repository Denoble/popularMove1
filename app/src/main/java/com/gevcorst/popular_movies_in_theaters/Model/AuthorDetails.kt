package com.gevcorst.popular_movies_in_theaters.Model


import android.os.Parcelable
import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass
import kotlinx.parcelize.Parcelize

@Parcelize
@JsonClass(generateAdapter = true)
data class AuthorDetails(
    @Json(name = "avatar_path")
    val avatarPath: String?,
    @Json(name = "name")
    val name: String,
    @Json(name = "rating")
    val rating: Int,
    @Json(name = "username")
    val username: String
):Parcelable