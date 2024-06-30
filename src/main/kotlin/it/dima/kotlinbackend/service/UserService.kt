package it.dima.kotlinbackend.service

import it.dima.kotlinbackend.dto.UserDTO
import it.dima.kotlinbackend.entity.User
import it.dima.kotlinbackend.repository.UserRepository
import mu.KLogging
import org.springframework.stereotype.Service

@Service
class UserService(val userRepository: UserRepository) {

    companion object: KLogging()

    fun addUser(userDTO: UserDTO): UserDTO{

        val userEntity = userDTO.let{
            User(null, it.full_name, it.email, it.password, it.meta_api_key, it.google_api_key, it.profile_picture)
        }
        userRepository.save(userEntity)

        logger.info("Saved course is $userEntity")

        return userEntity.let {
            UserDTO(it.id, it.full_name, it.email, it.password, it.meta_api_key, it.google_api_key, it.profile_picture)
        }
    }
}