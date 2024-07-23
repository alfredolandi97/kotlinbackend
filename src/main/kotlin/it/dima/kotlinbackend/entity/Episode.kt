package it.dima.kotlinbackend.entity

import jakarta.persistence.*
import java.io.Serializable

/*internal class EpisodeCompositeKey : Serializable {
    private val season = -1
    private val episode = -1

    companion object {
        private const val serialVersionUID = 1L
    }
}*/

data class EpisodeCompositeKey(
    val season: Int,
    val episode: Int,
)

@Entity
@IdClass(EpisodeCompositeKey::class)
data class Episode(

    @Id
    val season: Int,
    @Id
    val episode: Int,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "INSTRUCTOR_ID", nullable = false)
    val series: Series? = null
){
    override fun toString(): String {
        return "Episode(season=$season, episode=$episode, series='${series!!.seriesId}')"
    }
}
