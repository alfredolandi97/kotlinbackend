/*package it.dima.kotlinbackend.service

import it.dima.kotlinbackend.dto.SeenDTO
import it.dima.kotlinbackend.entity.Seen
import it.dima.kotlinbackend.repository.UserRepository
import it.dima.kotlinbackend.repository.WatchlistRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class WatchListService(val userService: UserService, val watchlistRepository: WatchlistRepository) {
    companion object: KLogging()

    fun addWatchedEpisode(seenDTO: SeenDTO): SeenDTO {
        val userOptional = userService.findByUserId(seenDTO.userId!!)

        if(!userOptional.isPresent){
            //TO-D0: Ad-hoc exception
            throw Exception("Instructor Not Valid for the Id: ${seenDTO.userId}")
        }

        val seenEntity = seenDTO.let{
            Seen(userOptional.get(), it.seriesId, it.episodeId)
        }
        watchlistRepository.save(seenEntity)

        logger.info("Saved course is $seenEntity")

        return seenEntity.let {
            SeenDTO(it.user!!.id, it.series_id, it.episode_id)
        }
    }

    fun retrieveWatchedEpisodesByUserId(userId: String): List<SeenDTO> {
        val watchedEpisodes = userId.let {
            watchlistRepository.findWatchedEpisodesByUserId(userId.toLong())
        }

        return watchedEpisodes.map {
            SeenDTO(it.user!!.id, it.series_id, it.episode_id)
        }
    }
}*/