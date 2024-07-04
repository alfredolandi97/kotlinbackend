package it.dima.kotlinbackend.dto

import jakarta.validation.constraints.NotBlank

data class SeenDTO(
    @get:NotBlank(message = "seenDTO.userId must not be blank")
    val userId : Long?,
    @get:NotBlank(message = "seenDTO.seriesId must not be blank")
    val seriesId : Long,
    @get:NotBlank(message = "seenDTO.episodeId must not be blank")
    val episodeId: Long,
)