package it.dima.kotlinbackend.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import it.dima.kotlinbackend.dto.UserDTO
import it.dima.kotlinbackend.service.UserService
import it.dima.kotlinbackend.utils.userDTO
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.reactive.AutoConfigureWebTestClient
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.test.web.reactive.server.WebTestClient

@WebMvcTest(controllers = [UserController::class])
@AutoConfigureWebTestClient
class UserControllerUnitTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @MockkBean
    lateinit var userServiceMock: UserService

    @Test
    fun addSubscriptionUser(){
        val userDTO = UserDTO(null, "Donatella Sciuto", "rettore@polimi.it", "polimi", "", "", "")

        every { userServiceMock.addUser(any()) } returns userDTO(id = 1)

        val subscribedUserDTO = webTestClient
            .post()
            .uri("/v1/users")
            .bodyValue(userDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(UserDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue{
            subscribedUserDTO!!.id != null &&
                    subscribedUserDTO.password != null
        }
    }
}