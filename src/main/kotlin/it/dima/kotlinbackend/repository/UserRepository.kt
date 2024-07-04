package it.dima.kotlinbackend.repository

import it.dima.kotlinbackend.dto.UserDTO
import it.dima.kotlinbackend.entity.User
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.CrudRepository
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import java.util.*

interface UserRepository: CrudRepository<User, Long> {
    fun findByEmailAndPasswordContaining(email: String, password: String): User

    @Query(value="SELECT * FROM user WHERE meta_api_key = 1 or google_api_key = 2", nativeQuery = true)
    fun findByExternalKey(meta_api_key: String?, google_api_key: String?): Optional<User>
}