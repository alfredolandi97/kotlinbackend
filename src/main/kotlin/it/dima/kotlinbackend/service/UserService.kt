package it.dima.kotlinbackend.service

import it.dima.kotlinbackend.dto.UserDTO
import it.dima.kotlinbackend.entity.User
import it.dima.kotlinbackend.exception.InvalidSubscriptionException
import it.dima.kotlinbackend.exception.UserNotFoundException
import it.dima.kotlinbackend.exception.UserNotValidException
import it.dima.kotlinbackend.repository.UserRepository
import mu.KLogging
import org.springframework.stereotype.Service
import java.util.*

@Service
class UserService(val userRepository: UserRepository) {

    companion object: KLogging()

    fun addUser(userDTO: UserDTO): UserDTO{
        //Checks about multiple identical subcriptions
        if(userDTO.meta_api_key != null || userDTO.google_api_key!= null) {
            var userOptional: Optional<User>
            if (userDTO.meta_api_key != null){
                userOptional = userRepository.findByMetaApiKeyContaining(userDTO.meta_api_key)
            }else if(userDTO.google_api_key != null){
                userOptional = userRepository.findByGoogleApiKeyContaining(userDTO.google_api_key)
            }else{
                throw UserNotValidException("Unrecognized user for this api key")
            }

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
        }else{
            //Checking password field validity for custom subscription
            if(userDTO.password == null){
                throw InvalidSubscriptionException("Password cannot be unspecified")
            }

            //Checking if all the fields are valid
            if(userDTO.full_name.isEmpty() || userDTO.email.isEmpty() || userDTO.password.isEmpty()){
                throw InvalidSubscriptionException("Invalid subscription fields")
            }
            val userResult = userRepository.findByEmailContaining(userDTO.email)

            //Checking if email is already taken or not
            if(userResult.isPresent){
                throw InvalidSubscriptionException("Email already taken")
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

        val userOptional = userRepository.findByEmailAndPasswordContaining(credentials["email"].toString(), credentials["password"].toString())
        val userEntity: User
        if(!userOptional.isPresent){
            throw UserNotFoundException("User not found for credentials email: ${credentials["email"]}, password: ${credentials["password"]}")
        }else{
            userEntity = userOptional.get()
        }

        return userEntity
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