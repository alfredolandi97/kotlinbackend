package it.dima.kotlinbackend.repository

import it.dima.kotlinbackend.entity.Episode
import it.dima.kotlinbackend.entity.EpisodeCompositeKey
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface EpisodeRepository: CrudRepository<Episode, EpisodeCompositeKey> {
    @Query(value="SELECT * FROM episode WHERE series_id = ?1 and season = ?2 and episode = ?3", nativeQuery = true)
    fun findEpisodeByCompositeKey(seriesId: Long, season: Int, episode: Int): Optional<Episode>
}