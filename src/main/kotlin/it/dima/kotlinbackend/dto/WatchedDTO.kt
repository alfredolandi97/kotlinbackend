package it.dima.kotlinbackend.dto

data class WatchedDTO(
    val userId: Long,
    val seriesId: Long,
    val season: Int,
    val episode: Int,
)