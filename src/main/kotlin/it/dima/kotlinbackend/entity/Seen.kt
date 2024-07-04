/*package it.dima.kotlinbackend.entity

import jakarta.persistence.*
import java.io.Serializable


class SeenId (
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    val user: User,
    val series_id : Long,
    val episode_id: Long,
): Serializable

@Entity
@Table(name = "seen")
@IdClass(SeenId::class)
data class Seen(
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    val user: User,
    @Id
    val series_id : Long,
    @Id
    val episode_id: Long,
){
    override fun toString(): String {
        return "Follow(user_id='${user!!.id}, series_id=$series_id, episode_id=$episode_id)')"
    }
}*/
