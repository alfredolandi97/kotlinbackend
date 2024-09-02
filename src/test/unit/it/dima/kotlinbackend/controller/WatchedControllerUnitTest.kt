package it.dima.kotlinbackend.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import it.dima.kotlinbackend.dto.*
import it.dima.kotlinbackend.service.UserService
import it.dima.kotlinbackend.service.WatchedService
import it.dima.kotlinbackend.utils.*
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [WatchedController::class])
@AutoConfigureWebTestClient
class WatchedControllerUnitTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var watchedServiceMock: WatchedService

    @Test
    fun addWatchedEpisodeTest(){
        val watchedDTO = WatchedDTO(2, 47145, 5, 2)

        every { watchedServiceMock.addWatchedEpisode(any()) } returns watchedDTO(userId = 2)

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
    fun getWatchedEpisodesTest(){
        every { watchedServiceMock.getWatchedEpisodesBySeries(any(), any()) }.returnsMany(
            listOf(
                episodeDTO(season = 1, episode = 2),
                episodeDTO(season = 2, episode = 3),
            )
        )

        val episodeDTOs = webTestClient
            .get()
            .uri("/v1/watched?user_id=2&series_id=47145")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(EpisodeDTO::class.java)
            .returnResult()
            .responseBody

        println("episodeDTOs: $episodeDTOs")
        assertEquals(2, episodeDTOs!!.size)
    }

    @Test
    fun deleteFavoriteTest(){
        every { watchedServiceMock.deleteWatchedService(any(), any(), any(), any()) } just runs

        webTestClient
            .delete()
            .uri("/v1/watched?user_id=2&series_id=47145&season=5&episode=2")
            .exchange()
            .expectStatus().isOk
    }
}