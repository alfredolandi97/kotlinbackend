package it.dima.kotlinbackend.dto

import jakarta.validation.constraints.NotNull


data class EpisodeDTO(
    @get:NotNull(message = "watchedDTO.season must not be blank")
    val season: Int,
    @get:NotNull(message = "watchedDTO.episode must not be blank")
    val episode: Int,
    //val name: String,
    //val airDate: String,
    //val watched: Boolean = false,
)