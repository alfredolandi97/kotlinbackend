package it.dima.kotlinbackend.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class UserDTO(
    val id: Long?,
    @get:NotBlank(message = "courseDTO.name must not be blank")
    val name: String
)