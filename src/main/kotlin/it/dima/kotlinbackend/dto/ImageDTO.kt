package it.dima.kotlinbackend.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class ImageDTO (
    @get:NotNull(message = "imageDTO.userId must not be null")
    val userId: Long,
    @get:NotBlank(message = "imageDTO.profilePicture must not be blank")
    val profilePicture: String,
)