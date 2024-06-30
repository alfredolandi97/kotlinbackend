package it.dima.kotlinbackend.entity

import jakarta.persistence.*

@Entity
@Table(name = "Users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long?,
    var full_name: String,
    var email: String,
    var password: String?,
    var meta_api_key: String?,
    var google_api_key: String?,
    var profile_picture: String?,
)
