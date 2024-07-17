package it.dima.kotlinbackend.utils

import com.fasterxml.jackson.annotation.JsonIgnoreProperties


data class Episode(
    val season: Int,
    val episode: Int,
    val name: String,
    val air_date: String
)

data class Details(
    val id: Long,
    val name: String?,
    val permalink: String?,
    val url: String?,
    val description: String?,
    val descriptionSource: String?,
    val start_date: String?,
    val end_date: String?,
    val country: String?,
    val status: String?,
    val runtime: String?,
    val network: String?,
    val youtube_link: String?,
    val image_path: String?,
    val image_thumbnail_path: String?,
    val rating: String?,
    val rating_count: String?,
    val countdown: Episode?,
    val genres: List<String>?,
    val pictures: List<String>?,
    val episodes: List<Episode>?
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class SeriesDetailsReply(
    val tvShow: Details
)