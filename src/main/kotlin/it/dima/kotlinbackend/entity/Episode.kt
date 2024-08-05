package it.dima.kotlinbackend.entity

import jakarta.persistence.*
import java.io.Serializable

@Embeddable
class EpisodeCompositeKey(
    val season:Int,
    val episode:Int,
    @Column(name = "series_id",)
    val seriesId: Long,
): Serializable

@Entity
data class Episode(

    @EmbeddedId
    val id:EpisodeCompositeKey,


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "series_id", referencedColumnName = "series_id", nullable = false, insertable=false, updatable=false)
    val series: Series,

    @ManyToMany(mappedBy = "episodes")
    val users: MutableList<User> = mutableListOf(),
){
    override fun toString(): String {
        return "Episode(season=${id.season}, episode=${id.episode}, series='${series!!.seriesId}')"
    }
}
