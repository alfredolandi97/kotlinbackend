package it.dima.kotlinbackend.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import it.dima.kotlinbackend.dto.SeriesDTO
import it.dima.kotlinbackend.dto.UserDTO
import it.dima.kotlinbackend.service.SeriesService
import it.dima.kotlinbackend.service.UserService
import it.dima.kotlinbackend.utils.seriesDTO
import it.dima.kotlinbackend.utils.userDTO
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [SeriesController::class])
@AutoConfigureWebTestClient
class SeriesControllerUnitTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var seriesServiceMock: SeriesService

    @Test
    fun getMostPopularSeriesTest() {
        every { seriesServiceMock.getMostPopular() }.returnsMany(
            listOf(seriesDTO(id = 1, name = "Test series 1"),
                seriesDTO(id = 2, name = "Test series 2"),)
        )

        val seriesDTOs = webTestClient
            .get()
            .uri("/v1/series/most_popular")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(SeriesDTO::class.java)
            .returnResult()
            .responseBody

        println("seriesDTOs: $seriesDTOs")
        assertEquals(2, seriesDTOs!!.size)
    }

    @Test
    fun getSeriesByQueryTest(){
        every { seriesServiceMock.getSeriesByQuery(any()) }.returnsMany(
            listOf(seriesDTO(id = 1, name = "Test series 1"),
                seriesDTO(id = 2, name = "Test series 2"),)
        )

        val seriesDTOs = webTestClient
            .get()
            .uri("/v1/series/search?q=test")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(SeriesDTO::class.java)
            .returnResult()
            .responseBody

        println("seriesDTOs: $seriesDTOs")
        assertEquals(2, seriesDTOs!!.size)
    }

    @Test
    fun getSeriesByIdTest(){
        every { seriesServiceMock.getSeriesById(1) } returns seriesDTO(id=1, "Test")


        val seriesDTO = webTestClient
            .get()
            .uri("/v1/series?series_id=1")
            .exchange()
            .expectStatus().isOk
            .expectBody(SeriesDTO::class.java)
            .returnResult()
            .responseBody

        println("seriesDTO: $seriesDTO")
        assertEquals(1, seriesDTO!!.id)
    }
}