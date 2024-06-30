package it.dima.kotlinbackend.dto

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class UserDTO(
    val id: Long?,
    @get:NotBlank(message = "userDTO.fullName must not be blank")
    val fullName: String,
    @get:NotBlank(message = "userDTO.email must not be blank")
    val email: String,
    val password: String?,
    val metaApiKey: String?,
    val googleApiKey: String?,
    val profilePicture: String?,
)