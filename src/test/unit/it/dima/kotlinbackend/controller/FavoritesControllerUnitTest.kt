package it.dima.kotlinbackend.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import it.dima.kotlinbackend.dto.FollowDTO
import it.dima.kotlinbackend.dto.SeriesDTO
import it.dima.kotlinbackend.dto.UserDTO
import it.dima.kotlinbackend.service.FavoritesService
import it.dima.kotlinbackend.service.UserService
import it.dima.kotlinbackend.utils.followDTO
import it.dima.kotlinbackend.utils.seriesDTO
import it.dima.kotlinbackend.utils.userDTO
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [FavoritesController::class])
@AutoConfigureWebTestClient
class FavoritesControllerUnitTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var favoritesServiceMock: FavoritesService

    @Test
    fun addFavoriteTest(){
        val followDTO = FollowDTO(2, 47145)

        every { favoritesServiceMock.addFavoriteSeries(any()) } returns followDTO(userId = 2)

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
    fun retrieveAllFavoritesByUserIdTest(){
        every { favoritesServiceMock.retrieveAllFavoritesByUserId(any()) }.returnsMany(
            listOf(
                seriesDTO(id = 1),
                seriesDTO(id = 2, name = "Test Series 2")
            )
        )

        val seriesDTOs = webTestClient
            .get()
            .uri("/v1/favorites?user_id=2")
            .exchange()
            .expectStatus().isOk
            .expectBodyList(SeriesDTO::class.java)
            .returnResult()
            .responseBody

        println("seriesDTOs: $seriesDTOs")
        assertEquals(2, seriesDTOs!!.size)
    }

    @Test
    fun deleteFavoriteTest(){
        every { favoritesServiceMock.deleteFavorite(any(), any()) } just runs

        webTestClient
            .delete()
            .uri("/v1/favorites?user_id=2&series_id=47145")
            .exchange()
            .expectStatus().isOk
    }


}