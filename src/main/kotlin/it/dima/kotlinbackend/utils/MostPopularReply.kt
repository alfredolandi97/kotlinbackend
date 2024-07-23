package it.dima.kotlinbackend.utils

import com.fasterxml.jackson.annotation.JsonIgnoreProperties

data class MostPopularDetails(
    val id: Long?,
    val name: String?,
    val permalink: String?,
    val start_date: String?,
    val end_date: String?,
    val country: String?,
    val network: String?,
    val status: String?,
    val image_thumbnail_path: String?,
)

@JsonIgnoreProperties(ignoreUnknown = true)
data class MostPopularReply(
    val total: String,
    val page: Long,
    val pages: Long,
    val tv_shows: List<MostPopularDetails>,
)
