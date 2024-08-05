package it.dima.kotlinbackend.service

import it.dima.kotlinbackend.entity.Episode
import it.dima.kotlinbackend.entity.EpisodeCompositeKey
import it.dima.kotlinbackend.entity.User
import it.dima.kotlinbackend.exception.SeriesNotFoundException
import it.dima.kotlinbackend.repository.EpisodeRepository
import mu.KLogging
import org.springframework.stereotype.Service
import java.util.*

@Service
class EpisodeService(
    val seriesService: SeriesService,
    val episodeRepository: EpisodeRepository,
) {
    companion object: KLogging()

    /*User can be null because this method is also developed for future use
    *that do not involve watch relationship
    */
    fun addEpisode(seriesId: Long, season: Int, episode: Int, user: User?): Episode {
        val seriesOptional = seriesService.findSeriesById(seriesId)
        if(!seriesOptional.isPresent){
            throw SeriesNotFoundException("That series does not have this season and this episode")
        }

        val episodeEntity = Episode(EpisodeCompositeKey(season, episode, seriesId), seriesOptional.get())
        if(user != null){
            episodeEntity.users.add(user)
        }

        episodeRepository.save(episodeEntity)

        return episodeEntity
    }

    fun findEpisodeByCompositeKey(seriesId: Long, season: Int, episode: Int): Optional<Episode> {
        return episodeRepository.findEpisodeByCompositeKey(seriesId, season, episode)
    }

    fun updateEpisode(episode: Episode){
        episodeRepository.save(episode)
    }
}