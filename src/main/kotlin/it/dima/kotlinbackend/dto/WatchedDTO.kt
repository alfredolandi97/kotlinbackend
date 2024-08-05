package it.dima.kotlinbackend.dto

import jakarta.validation.constraints.NotNull

data class WatchedDTO(
    @get:NotNull(message = "watchedDTO.userId must not be blank")
    val userId: Long,
    @get:NotNull(message = "watchedDTO.seriesId must not be blank")
    val seriesId: Long,
    @get:NotNull(message = "watchedDTO.season must not be blank")
    val season: Int,
    @get:NotNull(message = "watchedDTO.episode must not be blank")
    val episode: Int,
)