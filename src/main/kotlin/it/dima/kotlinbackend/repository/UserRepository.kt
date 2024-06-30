package it.dima.kotlinbackend.repository

import it.dima.kotlinbackend.entity.User
import org.springframework.data.repository.CrudRepository

interface UserRepository: CrudRepository<User, Long> {
}