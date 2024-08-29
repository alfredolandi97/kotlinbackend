package it.dima.kotlinbackend.utils

import it.dima.kotlinbackend.dto.UserDTO

fun userDTO(
    id: Long? = null,
    full_name: String = "Kotlin",
    email: String = "test@gmail.com",
    password: String? = null,
    meta_api_key: String = "",
    google_api_key: String = "",
    profile_picture: String = "",
) = UserDTO(
    id,
    full_name,
    email,
    password,
    meta_api_key,
    google_api_key,
    profile_picture,
)

