package it.dima.kotlinbackend.controller

import it.dima.kotlinbackend.dto.FollowDTO
import it.dima.kotlinbackend.dto.SeriesDTO
import it.dima.kotlinbackend.exception.SeriesNotFoundException
import it.dima.kotlinbackend.exception.UserNotFoundException
import it.dima.kotlinbackend.exception.UserNotValidException
import it.dima.kotlinbackend.repository.SeriesRepository
import it.dima.kotlinbackend.repository.UserRepository
import it.dima.kotlinbackend.utils.seriesEntityList
import it.dima.kotlinbackend.utils.userEntityList
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.reactive.server.WebTestClient

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@AutoConfigureWebTestClient
class FavoritesControllerIntgTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var seriesRepository: SeriesRepository

    @BeforeEach
    fun setUp() {
        userRepository.deleteAll()
        seriesRepository.deleteAll()

        val users = userEntityList()
        userRepository.saveAll(users)
        val series = seriesEntityList()
        seriesRepository.saveAll(series)
    }

    @Test
    fun addFavoriteTest_existingSeries(){
        val user = userRepository.findByEmailAndPasswordContaining("alfredo.landi@mail.polimi.it", "landi")
        val series = seriesRepository.findById(47145)
        val followDTO = FollowDTO(user.get().id!!, series.get().seriesId)

        val response = webTestClient
            .post()
            .uri("/v1/favorites")
            .bodyValue(followDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(FollowDTO::class.java)
            .returnResult()
            .responseBody

        assertEquals(followDTO.userId, response!!.userId)
        assertEquals(followDTO.seriesId, response!!.seriesId)
    }

    @Test
    fun addFavoriteTest_newSeries(){
        val user = userRepository.findByEmailAndPasswordContaining("alfredo.landi@mail.polimi.it", "landi")
        val followDTO = FollowDTO(user.get().id!!, 35420)

        val response = webTestClient
            .post()
            .uri("/v1/favorites")
            .bodyValue(followDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(FollowDTO::class.java)
            .returnResult()
            .responseBody

        assertEquals(followDTO.userId, response!!.userId)
        assertEquals(followDTO.seriesId, response!!.seriesId)
    }

    @Test
    fun addFavoriteTest_userNotFound(){
        val followDTO = FollowDTO(-1, -1)

        val response = webTestClient
            .post()
            .uri("/v1/favorites")
            .bodyValue(followDTO)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(
            UserNotFoundException(
                "User Not Found"
            ).toString(),
            UserNotFoundException(response!!)
                .toString()
        )

    }

    @Test
    fun retrieveAllFavoritesByUserIdTest(){
        val email = "alfredo.landi@mail.polimi.it"
        val password = "landi"
        val user = userRepository.findByEmailAndPasswordContaining(email, password)
        val series = seriesRepository.findById(47145)

        val userEntity = user.get()
        userEntity.series = mutableListOf(series.get())
        userRepository.save(userEntity)

        val seriesEntity = series.get()
        seriesEntity.users = mutableListOf(userEntity)
        seriesRepository.save(seriesEntity)

        val seriesDTOs = webTestClient
            .get()
            .uri("/v1/favorites?user_id={userId}", user.get().id)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(SeriesDTO::class.java)
            .returnResult()
            .responseBody

        println("seriesDTOs: $seriesDTOs")
        assertEquals(1, seriesDTOs!!.size)
    }

    @Test
    fun retrieveAllFavoritesByUserIdTest_invalidUser(){
        val response = webTestClient
            .get()
            .uri("/v1/favorites?user_id={userId}", -1L)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(
            UserNotValidException("Invalid User to retrieve favorites").toString(),
            UserNotValidException(response!!)
                .toString()
        )
    }

    @Test
    fun deleteFavoriteTest_invalidUserId(){
        val response = webTestClient
            .delete()
            .uri("/v1/favorites?user_id={userId}&series_id={seriesId}", -1L, -1L)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(
            UserNotFoundException("User does not exist, unable to delete this favorite").toString(),
            UserNotFoundException(response!!)
                .toString()
        )
    }

    @Test
    fun deleteFavoriteTest_invalidSeriesId(){
        val email = "alfredo.landi@mail.polimi.it"
        val password = "landi"
        val user = userRepository.findByEmailAndPasswordContaining(email, password)

        val userEntity = user.get()
        userRepository.save(userEntity)

        val response = webTestClient
            .delete()
            .uri("/v1/favorites?user_id={userId}&series_id={seriesId}", user.get().id, -1)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(
            SeriesNotFoundException("This series seems not to exist, unable to delete this favorite").toString(),
            SeriesNotFoundException(response!!)
                .toString()
        )
    }
}