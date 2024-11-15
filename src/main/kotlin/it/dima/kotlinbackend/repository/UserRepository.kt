package it.dima.kotlinbackend.repository

import it.dima.kotlinbackend.entity.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import java.util.*

interface UserRepository: CrudRepository<User, Long> {
    @Query(value="SELECT * FROM seriestime_user WHERE email = ?1 and password = ?2", nativeQuery = true)
    fun findByEmailAndPasswordContaining(email: String, password: String): Optional<User>

    @Query(value="SELECT * FROM seriestime_user WHERE meta_api_key = ?1", nativeQuery = true)
    fun findByMetaApiKeyContaining(meta_api_key: String): Optional<User>

    @Query(value="SELECT * FROM seriestime_user WHERE google_api_key = ?1", nativeQuery = true)
    fun findByGoogleApiKeyContaining(google_api_key: String): Optional<User>

    @Query(value="SELECT * FROM seriestime_user WHERE email = ?1", nativeQuery = true)
    fun findByEmailContaining(email: String): Optional<User>
}