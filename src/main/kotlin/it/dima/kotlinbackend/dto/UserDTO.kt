package it.dima.kotlinbackend.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class UserDTO(
    val id: Long?,
    @get:NotBlank(message = "userDTO.fullName must not be blank")
    val full_name: String,
    @get:NotBlank(message = "userDTO.email must not be blank")
    val email: String,
    val password: String?,
    val meta_api_key: String?,
    val google_api_key: String?,
    val profile_picture: String?,
)