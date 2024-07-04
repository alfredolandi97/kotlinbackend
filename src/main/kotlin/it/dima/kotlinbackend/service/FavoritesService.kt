package it.dima.kotlinbackend.service

import it.dima.kotlinbackend.dto.FollowDTO
import it.dima.kotlinbackend.entity.Series
import it.dima.kotlinbackend.entity.User
import it.dima.kotlinbackend.repository.SeriesRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class FavoritesService(val userService: UserService, val seriesRepository: SeriesRepository) {
    companion object: KLogging()

    fun addFavoriteSeries(followDTO: FollowDTO): FollowDTO {
        val userOptional = userService.findByUserId(followDTO.userId)

        if(!userOptional.isPresent){
            //TO-D0: Ad-hoc exception
            throw Exception("User Not Valid for the Id: ${followDTO.userId}")
        }

        val seriesOptional = seriesRepository.findById(followDTO.seriesId)
        var seriesEntity: Series
        if(!seriesOptional.isPresent){
            //Save new series
            seriesEntity = followDTO.let {
                Series(
                    it.seriesId,
                    mutableListOf(userOptional.get())
                )
            }
            seriesRepository.save(seriesEntity)
        }else{
            //Update existing series
            seriesEntity = seriesOptional.get()
            seriesEntity.users.add(userOptional.get())
            seriesRepository.save(seriesEntity)
        }

        //Update user
        var user = userOptional.get()
        user.series.add(seriesEntity)
        userService.updateUser(user)


        return seriesEntity.let {
            FollowDTO(it.users.last().id!!, it.seriesId)
        }
    }
}