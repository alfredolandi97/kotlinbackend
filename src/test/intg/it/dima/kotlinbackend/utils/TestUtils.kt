package it.dima.kotlinbackend.utils

import it.dima.kotlinbackend.dto.*
import it.dima.kotlinbackend.entity.User
import it.dima.kotlinbackend.entity.Episode
import it.dima.kotlinbackend.entity.EpisodeCompositeKey
import it.dima.kotlinbackend.entity.Series
import it.dima.kotlinbackend.utils.*


fun userDTO(
    id: Long? = null,
    full_name: String = "Test User",
    email: String = "test@gmail.com",
    password: String? = "iamatest",
    meta_api_key: String? = null,
    google_api_key: String? = null,
    profile_picture: String? = null,
) = UserDTO(
    id,
    full_name,
    email,
    password,
    meta_api_key,
    google_api_key,
    profile_picture,
)

fun seriesDTO(
    id: Long? = 1,
    name: String? = "Test series",
    description: String? = null,
    startDate: String? = null,
    status: String? = null,
    network: String? = null,
    thumbnail: String? = null,
    rating: String? = null,
    genres: List<String>? = null,
    countdown: it.dima.kotlinbackend.utils.Episode? = null,
    episodes: List<it.dima.kotlinbackend.utils.Episode>? = null
) = SeriesDTO(
    id,
    name,
    description,
    startDate,
    status,
    network,
    thumbnail,
    rating,
    genres,
    countdown,
    episodes
)

fun followDTO(
    userId: Long = 2,
    seriesId: Long = 47145
) = FollowDTO(
    userId,
    seriesId
)

fun watchedDTO(
    userId: Long = 2,
    seriesId: Long = 47145,
    season: Int = 5,
    episode: Int = 2,
) = WatchedDTO(
    userId,
    seriesId,
    season,
    episode
)

fun episodeDTO(
    season: Int = 5,
    episode: Int = 2
) = EpisodeDTO(
    season,
    episode
)

fun userEntityList() = mutableListOf(
    User(null,
        "Federico Lamperti",
        "federico.lamperti@mail.polimi.it",
        "lamperti",
        null,
        null,
        null
    ),
    User(null,
        "Alfredo Landi",
        "alfredo.landi@mail.polimi.it",
        "landi",
        null,
        null,
        null
    ),
    User(null,
        "Federico Lamperti",
        "crielu@libero.it",
        null,
        "USyvXZWcBuX0zGtk3lCFkUthVW83",
        null,
        "https://graph.facebook.com/2776099672548504/picture"
    ),
    User(null,
        "Federico Lamperti",
        "fedelampe11@gmail.com",
        null,
        null,
        "qAuhHd4EssQHTdEh1zJE1Qdtl2p1",
        null
    )
)

fun seriesEntityList() = listOf(
    Series(
        47145,
    )
)

fun episodeEntityList(users: MutableList<User>, series: Series) = listOf(
    Episode(
        EpisodeCompositeKey(
            1,
            1,
            47145,
        ),
        series,
        users,
    ),
    Episode(
        EpisodeCompositeKey(
            5,
            2,
            47145,
        ),
        series,
        users,
    )
)