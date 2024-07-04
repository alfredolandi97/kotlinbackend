package it.dima.kotlinbackend.service

import it.dima.kotlinbackend.dto.UserDTO
import it.dima.kotlinbackend.entity.User
import it.dima.kotlinbackend.repository.UserRepository
import mu.KLogging
import org.springframework.stereotype.Service
import java.security.InvalidKeyException
import java.util.*

@Service
class UserService(val userRepository: UserRepository) {

    companion object: KLogging()

    fun addUser(userDTO: UserDTO): UserDTO{
        if(userDTO.meta_api_key != null || userDTO.google_api_key!= null) {
            val userOptional = userRepository.findByExternalKey(userDTO.meta_api_key, userDTO.google_api_key)
            if (userOptional.isPresent) {
                return userOptional.get().let {
                    UserDTO(
                        it.id,
                        it.full_name,
                        it.email,
                        it.password,
                        it.meta_api_key,
                        it.google_api_key,
                        it.profile_picture
                    )
                }
            }
        }

        val userEntity = userDTO.let {
            User(
                null,
                it.full_name,
                it.email,
                it.password,
                it.meta_api_key,
                it.google_api_key,
                it.profile_picture
            )
        }
        userRepository.save(userEntity)

        logger.info("Saved course is $userEntity")

        return userEntity.let {
            UserDTO(
                it.id,
                it.full_name,
                it.email,
                it.password,
                it.meta_api_key,
                it.google_api_key,
                it.profile_picture,
            )
        }

    }

    fun retrieveUser(email: String, password: String): UserDTO {
        val credentials = mapOf(
            "email" to email,
            "password" to password
        )

        val user = credentials.let {
            userRepository.findByEmailAndPasswordContaining(credentials["email"].toString(), credentials["password"].toString())
        }

        return user
            .let{
                UserDTO(it.id,it.full_name, it.email, it.password, it.meta_api_key, it.google_api_key, it.profile_picture)
            }

    }

    fun findByUserId(userId: Long): Optional<User> {
        return userRepository.findById(userId)
    }

    fun updateUser(user: User) {
        userRepository.save(user)
    }
}