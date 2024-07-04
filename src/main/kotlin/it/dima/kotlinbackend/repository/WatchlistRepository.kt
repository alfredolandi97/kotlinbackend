/*package it.dima.kotlinbackend.repository

import it.dima.kotlinbackend.entity.Seen
import it.dima.kotlinbackend.entity.SeenId
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface WatchlistRepository: CrudRepository<Seen, SeenId> {
    @Query(value="SELECT series_id FROM seen WHERE user_id = 1", nativeQuery = true)
    fun findWatchedEpisodesByUserId(userId: Long): List<Seen>
}*/