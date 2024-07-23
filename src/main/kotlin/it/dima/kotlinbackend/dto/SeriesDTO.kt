package it.dima.kotlinbackend.dto

import it.dima.kotlinbackend.utils.Episode

data class SeriesDTO(
    val id: Long?,
    val name: String?,
    val description: String?,
    val startDate: String?,
    val status: String?,
    val network: String?,
    val thumbnail: String?,
    val rating: String?,
    val genres: List<String>?,
    val countdown: Episode?,
    val episodes: List<Episode>?
)
