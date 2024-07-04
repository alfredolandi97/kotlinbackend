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

    fun retrieveAllFavoritesByUserId(userId: Long): List<Long> {
        val user = userService.findByUserId(userId)
        if(user.isPresent){
            return user.get().series.map {
                it.seriesId
            }
        }else{
            //TO-DO: Custom exception
            throw Exception("User not found")
        }
    }

    fun deleteFavorite(userId: Long, seriesId: Long) {
        //Check User Validity
        val userOptional = userService.findByUserId(userId)
        val userEntity: User
        if(!userOptional.isPresent){
            //TO-DO Custom Exception
            throw Exception("User not found")
        }else{
            userEntity = userOptional.get()
        }

        //Check Series Validaty
        val seriesOptional = seriesRepository.findById(seriesId)
        //LEGACY CODE!!"
        //val seriesEntity: Series
        if(!seriesOptional.isPresent){
            //TO-DO Custom Exception
            throw Exception("Series not found")
        }
        //LEGACY CODE!!!
        /*else{
            seriesEntity = seriesOptional.get()
        }*/

        //Updating User
        userEntity.series.remove(
            userEntity.series.filter{ it.seriesId==seriesId }
                .first()
        )
        userService.updateUser(userEntity)

        //LEGACY CODE!!!
        /*logger.info("targer series to string " + seriesEntity.toString())
        //Updating Series
        seriesEntity.users.remove(
            seriesEntity.users.filter{ it.id!!.toLong()==userId }
                .first()
        )
        seriesRepository.save(seriesEntity)*/

    }
}