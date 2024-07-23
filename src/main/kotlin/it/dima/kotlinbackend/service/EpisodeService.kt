package it.dima.kotlinbackend.service

import it.dima.kotlinbackend.dto.EpisodeDTO
import it.dima.kotlinbackend.exception.SeriesNotFoundException
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class EpisodeService(val seriesService: SeriesService) {
    companion object: KLogging()
    fun addWatchedEpisode(episodeDTO: EpisodeDTO): EpisodeDTO{
        //Checking phase
        val seriesOptional = seriesService.findSeriesById(episodeDTO.seriesId)
        if(!seriesOptional.isPresent){
            throw SeriesNotFoundException("This series does not exist")
        }
        return episodeDTO
    }
}