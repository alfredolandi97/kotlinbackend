package it.dima.kotlinbackend.dto

import jakarta.persistence.Column

data class EpisodeDTO(
    val season: Int,
    val episode: Int,
    val name: String,
    val airDate: String
)