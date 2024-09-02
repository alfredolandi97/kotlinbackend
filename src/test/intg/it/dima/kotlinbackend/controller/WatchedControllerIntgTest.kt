package it.dima.kotlinbackend.controller

import io.mockk.every
import io.mockk.just
import it.dima.kotlinbackend.dto.EpisodeDTO
import it.dima.kotlinbackend.dto.FollowDTO
import it.dima.kotlinbackend.dto.SeriesDTO
import it.dima.kotlinbackend.dto.WatchedDTO
import it.dima.kotlinbackend.exception.EpisodeNotFoundException
import it.dima.kotlinbackend.exception.EpisodeNotValidException
import it.dima.kotlinbackend.exception.SeriesNotFoundException
import it.dima.kotlinbackend.exception.UserNotFoundException
import it.dima.kotlinbackend.repository.EpisodeRepository
import it.dima.kotlinbackend.repository.SeriesRepository
import it.dima.kotlinbackend.repository.UserRepository
import it.dima.kotlinbackend.utils.*
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
class WatchedControllerIntgTest: PostgresSQLContainerInitializer() {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var userRepository: UserRepository

    @Autowired
    lateinit var seriesRepository: SeriesRepository

    @Autowired
    lateinit var episodeRepository: EpisodeRepository



    @BeforeEach
    fun setUp() {
        userRepository.deleteAll()
        seriesRepository.deleteAll()
        episodeRepository.deleteAll()

        val users = userEntityList()
        userRepository.saveAll(users)
        val series = seriesEntityList()
        seriesRepository.saveAll(series)
        val episodes = episodeEntityList(users, series[0])
        episodeRepository.saveAll(episodes)

    }

    @Test
    fun addWatchedEpisodeTest_existingEpisode(){
        val email = "alfredo.landi@mail.polimi.it"
        val password = "landi"
        val user = userRepository.findByEmailAndPasswordContaining(email, password)
        val userEntity = user.get()

        val seriesId: Long = 47145
        val season = 5
        val episodeNumber = 2
        val episode = episodeRepository.findEpisodeByCompositeKey(seriesId, season, episodeNumber)
        val episodeEntity = episode.get()
        val watchedDTO = WatchedDTO(userEntity.id!!, episodeEntity.id.seriesId, episodeEntity.id.season, episodeEntity.id.episode)

        val response = webTestClient
            .post()
            .uri("/v1/watched")
            .bodyValue(watchedDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(watchedDTO::class.java)
            .returnResult()
            .responseBody

        assertEquals(watchedDTO.userId, response!!.userId)
        assertEquals(watchedDTO.seriesId, response!!.seriesId)
        assertEquals(watchedDTO.season, response!!.season)
        assertEquals(watchedDTO.episode, response!!.episode)
    }

    @Test
    fun addWatchedEpisodeTest_newEpisode(){
        val email = "alfredo.landi@mail.polimi.it"
        val password = "landi"
        val user = userRepository.findByEmailAndPasswordContaining(email, password)
        val userEntity = user.get()

        val seriesId: Long = 47145
        val season = 5
        val episodeNumber = 2
        val watchedDTO = WatchedDTO(userEntity.id!!, seriesId, season, episodeNumber)

        val response = webTestClient
            .post()
            .uri("/v1/watched")
            .bodyValue(watchedDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(watchedDTO::class.java)
            .returnResult()
            .responseBody

        assertEquals(watchedDTO.userId, response!!.userId)
        assertEquals(watchedDTO.seriesId, response!!.seriesId)
        assertEquals(watchedDTO.season, response!!.season)
        assertEquals(watchedDTO.episode, response!!.episode)
    }


    @Test
    fun addWatchedEpisodeTest_userNotFound(){

        val response = webTestClient
            .post()
            .uri("/v1/watched")
            .bodyValue(watchedDTO(-1, -1, -1, -1))
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(
            UserNotFoundException("This user does not exist").toString(),
            UserNotFoundException(response!!)
                .toString()
        )
    }

    @Test
    fun addWatchedEpisodeTest_seriesNotFound(){
        val email = "alfredo.landi@mail.polimi.it"
        val password = "landi"
        val user = userRepository.findByEmailAndPasswordContaining(email, password)
        val userEntity = user.get()

        val watchedDTO = WatchedDTO(userEntity.id!!, -1, -1, -1)

        val response = webTestClient
            .post()
            .uri("/v1/watched")
            .bodyValue(watchedDTO)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(
            SeriesNotFoundException("Series not found").toString(),
            SeriesNotFoundException(response!!)
                .toString()
        )
    }

    @Test
    fun addWatchedEpisodeTest_episodeAlreadyWatched(){
        val email = "alfredo.landi@mail.polimi.it"
        val password = "landi"
        val user = userRepository.findByEmailAndPasswordContaining(email, password)
        val userEntity = user.get()

        val seriesId: Long = 47145
        val series = seriesRepository.findById(47145)
        val seriesEntity = series.get()

        userEntity.series = mutableListOf(seriesEntity)
        seriesEntity.users = mutableListOf(userEntity)


        val season = 5
        val episodeNumber = 2
        val episode = episodeRepository.findEpisodeByCompositeKey(seriesId, season, episodeNumber)
        val episodeEntity = episode.get()

        userEntity.episodes = mutableListOf(episodeEntity)
        seriesEntity.users = mutableListOf(userEntity)
        episodeEntity.users = mutableListOf(userEntity)
        episodeEntity.series = seriesEntity


        userRepository.save(userEntity)
        seriesRepository.save(seriesEntity)
        episodeRepository.save(episodeEntity)

        val response = webTestClient
            .post()
            .uri("/v1/watched")
            .bodyValue(WatchedDTO(userEntity.id!!, seriesId, season, episodeNumber))
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(
            EpisodeNotValidException("Episode already marked as watched").toString(),
            EpisodeNotValidException(response!!)
                .toString()
        )
    }

    @Test
    fun getWatchedEpisodesTest(){
        val email = "alfredo.landi@mail.polimi.it"
        val password = "landi"
        val user = userRepository.findByEmailAndPasswordContaining(email, password)
        val userEntity = user.get()

        val seriesId: Long = 47145
        val series = seriesRepository.findById(47145)
        val seriesEntity = series.get()

        userEntity.series = mutableListOf(seriesEntity)
        seriesEntity.users = mutableListOf(userEntity)


        val season = 5
        val episodeNumber = 2
        val episode = episodeRepository.findEpisodeByCompositeKey(seriesId, season, episodeNumber)
        val episodeEntity = episode.get()

        userEntity.episodes = mutableListOf(episodeEntity)
        seriesEntity.users = mutableListOf(userEntity)
        episodeEntity.users = mutableListOf(userEntity)
        episodeEntity.series = seriesEntity


        userRepository.save(userEntity)
        seriesRepository.save(seriesEntity)
        episodeRepository.save(episodeEntity)


        val episodeDTOs = webTestClient
            .get()
            .uri("/v1/watched?user_id={userId}&series_id={seriesId}", userEntity.id, 47145)
            .exchange()
            .expectStatus().isOk
            .expectBodyList(EpisodeDTO::class.java)
            .returnResult()
            .responseBody

        println("episodeDTOs: $episodeDTOs")
        assertEquals(1, episodeDTOs!!.size)
    }

    @Test
    fun getWatchedEpisodesTest_userNotFound(){
        val response = webTestClient
            .get()
            .uri("/v1/watched?user_id={userId}&series_id={seriesId}", -1, -1)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(
            UserNotFoundException("User does not exist, severe error").toString(),
            UserNotFoundException(response!!)
                .toString()
        )
    }

    @Test
    fun getWatchedEpisodesTest_seriesNotFound(){
        val email = "alfredo.landi@mail.polimi.it"
        val password = "landi"
        val user = userRepository.findByEmailAndPasswordContaining(email, password)
        val userEntity = user.get()

        val response = webTestClient
            .get()
            .uri("/v1/watched?user_id={userId}&series_id={seriesId}", userEntity.id, -1)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(
            SeriesNotFoundException("Series not found").toString(),
            SeriesNotFoundException(response!!)
                .toString()
        )
    }

    @Test
    fun deleteFavoriteTest(){
        val email = "alfredo.landi@mail.polimi.it"
        val password = "landi"
        val user = userRepository.findByEmailAndPasswordContaining(email, password)
        val userEntity = user.get()

        val seriesId: Long = 47145
        val series = seriesRepository.findById(47145)
        val seriesEntity = series.get()

        userEntity.series = mutableListOf(seriesEntity)
        seriesEntity.users = mutableListOf(userEntity)

        val season = 5
        val episodeNumber = 2
        val episode = episodeRepository.findEpisodeByCompositeKey(seriesId, season, episodeNumber)
        val episodeEntity = episode.get()

        userEntity.episodes = mutableListOf(episodeEntity)
        seriesEntity.users = mutableListOf(userEntity)
        episodeEntity.users = mutableListOf(userEntity)
        episodeEntity.series = seriesEntity


        userRepository.save(userEntity)
        seriesRepository.save(seriesEntity)
        episodeRepository.save(episodeEntity)

        webTestClient
            .delete()
            .uri(
                "/v1/watched?user_id={userId}&series_id={seriesId}&season={season}&episode={episode}",
                userEntity.id,
                seriesId,
                season,
                episodeNumber
            )
            .exchange()
            .expectStatus().isOk
    }

    @Test
    fun deleteFavoriteTest_userNotFound() {

        val response = webTestClient
            .delete()
            .uri(
                "/v1/watched?user_id={userId}&series_id={seriesId}&season={season}&episode={episode}",
                -1,
                -1,
                -1,
                -1
            )
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(
            UserNotFoundException("This user seems it does not exist, unable to delete this watched episode").toString(),
            UserNotFoundException(response!!)
                .toString()
        )
    }

    @Test
    fun deleteFavoriteTest_seriesNotFound(){
        val email = "alfredo.landi@mail.polimi.it"
        val password = "landi"
        val user = userRepository.findByEmailAndPasswordContaining(email, password)
        val userEntity = user.get()


        val response = webTestClient
            .delete()
            .uri(
                "/v1/watched?user_id={userId}&series_id={seriesId}&season={season}&episode={episode}",
                userEntity.id,
                -1,
                -1,
                -1
            )
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(
            SeriesNotFoundException("This series seems not to exist, unable to delete this watched episode").toString(),
            SeriesNotFoundException(response!!)
                .toString()
        )
    }

    @Test
    fun deleteFavoriteTest_episodeNotFound(){
        val email = "alfredo.landi@mail.polimi.it"
        val password = "landi"
        val user = userRepository.findByEmailAndPasswordContaining(email, password)
        val userEntity = user.get()

        val seriesId: Long = 47145
        val series = seriesRepository.findById(47145)
        val seriesEntity = series.get()

        val response = webTestClient
            .delete()
            .uri(
                "/v1/watched?user_id={userId}&series_id={seriesId}&season={season}&episode={episode}",
                userEntity.id,
                seriesEntity.seriesId,
                -1,
                -1
            )
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(
            EpisodeNotFoundException("This episode seems not to exist, unable to delete this watched episode").toString(),
            EpisodeNotFoundException(response!!)
                .toString()
        )
    }

    @Test
    fun deleteFavoriteTest_episodeNotValid(){
        val email = "alfredo.landi@mail.polimi.it"
        val password = "landi"
        val user = userRepository.findByEmailAndPasswordContaining(email, password)
        val userEntity = user.get()

        val seriesId: Long = 47145
        val series = seriesRepository.findById(47145)
        val seriesEntity = series.get()

        userEntity.series = mutableListOf(seriesEntity)
        seriesEntity.users = mutableListOf(userEntity)

        val season = 5
        val episodeNumber = 2
        val episode = episodeRepository.findEpisodeByCompositeKey(seriesId, season, episodeNumber)
        val episodeEntity = episode.get()

        episodeEntity.series = seriesEntity

        userRepository.save(userEntity)
        seriesRepository.save(seriesEntity)
        episodeRepository.save(episodeEntity)

        val response = webTestClient
            .delete()
            .uri(
                "/v1/watched?user_id={userId}&series_id={seriesId}&season={season}&episode={episode}",
                userEntity.id,
                seriesId,
                season,
                episodeNumber
            )
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(
            EpisodeNotValidException("This episode was not watched by the user").toString(),
            EpisodeNotValidException(response!!)
                .toString()
        )
    }
}