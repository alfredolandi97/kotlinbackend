package it.dima.kotlinbackend.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class ImageDTO (
    @get:NotBlank(message = "imageDTO.userId must not be blank")
    val userId: String,
    @get:NotBlank(message = "imageDTO.profilePicture must not be blank")
    val profilePicture: String,
)