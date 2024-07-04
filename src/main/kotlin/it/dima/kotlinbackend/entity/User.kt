package it.dima.kotlinbackend.entity

import jakarta.persistence.*
import javax.sound.midi.Track

@Entity
@Table(name = "seriestime_user")
data class User(
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "user_id")
    val id: Long?,
    var full_name: String,
    var email: String,
    var password: String?,
    var meta_api_key: String?,
    var google_api_key: String?,
    var profile_picture: String?,

    @ManyToMany
    @JoinTable(
        name = "follow",
        joinColumns = arrayOf(JoinColumn(name = "user_id")),
        inverseJoinColumns = arrayOf(JoinColumn(name = "series_id"))
    )
    val series: MutableList<Series> = mutableListOf(),

)
