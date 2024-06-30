package it.dima.kotlinbackend.entity

import jakarta.persistence.*

@Entity
@Table(name = "Users")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    val id: Long?,
    var fullName: String,
    var email: String,
    var password: String?,
    var metaApiKey: String?,
    var googleApiKey: String?,
    var profilePicture: String?,
)
