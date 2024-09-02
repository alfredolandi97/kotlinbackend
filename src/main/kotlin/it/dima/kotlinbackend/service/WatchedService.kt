package it.dima.kotlinbackend.service

import it.dima.kotlinbackend.dto.EpisodeDTO
import it.dima.kotlinbackend.dto.WatchedDTO
import it.dima.kotlinbackend.entity.Episode
import it.dima.kotlinbackend.entity.User
import it.dima.kotlinbackend.exception.EpisodeNotFoundException
import it.dima.kotlinbackend.exception.EpisodeNotValidException
import it.dima.kotlinbackend.exception.SeriesNotFoundException
import it.dima.kotlinbackend.exception.UserNotFoundException
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class WatchedService(val userService: UserService, val seriesService: SeriesService, val episodeService: EpisodeService) {
    companion object: KLogging()
    fun addWatchedEpisode(watchedDTO: WatchedDTO): WatchedDTO{
        //Checking phase
        //UserId check
        val userOptional = userService.findByUserId(watchedDTO.userId)
        if(!userOptional.isPresent){
            throw UserNotFoundException("This user does not exist")
        }

        //SeriesId check
        val seriesOptional = seriesService.findSeriesById(watchedDTO.seriesId)
        if(!seriesOptional.isPresent){
            throw SeriesNotFoundException("Series not found")
        }

        //Episode check
        val episodeOptional = episodeService.findEpisodeByCompositeKey(
            watchedDTO.seriesId,
            watchedDTO.season,
            watchedDTO.episode
        )

        //Initializing watch relationship on the episode side
        val episodeEntity: Episode
        if(!episodeOptional.isPresent){
            episodeEntity = episodeService.addEpisode(watchedDTO.seriesId, watchedDTO.season, watchedDTO.episode, userOptional.get())
        }else{
            episodeEntity = episodeOptional.get()

            //Check if the watched episode is still existing
            val episode = episodeEntity.users.filter{
                it.id == watchedDTO.userId
            }
            if(episode.isNotEmpty()){
                throw EpisodeNotValidException("Episode already marked as watched")
            }

            episodeEntity.users.add(userOptional.get())
            episodeService.updateEpisode(episodeEntity)
        }

        //Finalizing watch relationship on the user side
        val user = userOptional.get()
        user.episodes.add(episodeEntity)
        userService.updateUser(user)

        return episodeEntity.let {
            WatchedDTO(it.users.last().id!!, it.series.seriesId, it.id.season, it.id.episode)
        }
    }

    fun deleteWatchedService(userId: Long, seriesId: Long, season: Int, episode: Int){
        //Check User Validity
        val userOptional = userService.findByUserId(userId)
        val userEntity: User
        if(!userOptional.isPresent){
            throw UserNotFoundException("This user seems it does not exist, unable to delete this watched episode")
        }else{
            userEntity = userOptional.get()
        }

        //Check Series Validaty
        val seriesOptional = seriesService.findSeriesById(seriesId)
        if(!seriesOptional.isPresent){
            throw SeriesNotFoundException("This series seems not to exist, unable to delete this watched episode")
        }

        //Check episode validity
        val episodeOptional = episodeService.findEpisodeByCompositeKey(seriesId, season, episode)

        if(!episodeOptional.isPresent){
            throw EpisodeNotFoundException("This episode seems not to exist, unable to delete this watched episode")
        }

        val episodes = userEntity.episodes.filter{
            it.id.seriesId == seriesId &&
                    it.id.season == season &&
                    it.id.episode == episode
        }

        if (episodes.isEmpty()){
            throw EpisodeNotValidException("This episode was not watched by the user")
        }else{
            userEntity.episodes.remove(
                episodes.first()
            )
            userService.updateUser(userEntity)
        }
    }

    fun getWatchedEpisodesBySeries(userId: Long, seriesId: Long): List<EpisodeDTO>{
        //Check User Validity
        val userOptional = userService.findByUserId(userId)
        val userEntity: User
        if(!userOptional.isPresent){
            throw UserNotFoundException("User does not exist, severe error")
        }else{
            userEntity = userOptional.get()
        }


        val series = userEntity.series.filter{
            it.seriesId == seriesId
        }
        if (series.isEmpty()){
            throw SeriesNotFoundException("Series not found")
        }

        val watchedEpisodes = userEntity.episodes.filter{
            it.id.seriesId == seriesId
        }

        return watchedEpisodes.map {
            EpisodeDTO(it.id.season, it.id.episode)
        }
    }
}