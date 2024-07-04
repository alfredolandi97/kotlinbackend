package it.dima.kotlinbackend.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotEmpty
import jakarta.validation.constraints.NotNull

data class FollowDTO(
    @get:NotNull(message = "followDTO.userId must not be null")
    val userId: Long,
    @get:NotNull(message = "followDTO.seriesId must not be null")
    val seriesId: Long,
)
