package it.dima.kotlinbackend.controller

import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import io.mockk.just
import io.mockk.runs
import it.dima.kotlinbackend.dto.ImageDTO
import it.dima.kotlinbackend.dto.UserDTO
import it.dima.kotlinbackend.service.UserService
import it.dima.kotlinbackend.utils.userDTO
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.Assertions.assertEquals
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
    fun addUser(){
        val userDTO = UserDTO(null, "Test User", "test@gmail.com", "iamatest", null, null, null)

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
            subscribedUserDTO!!.id != null
        }
    }

    @Test
    fun addUser_validation(){
        val userDTO = UserDTO(null, "", "", "", "", "", "")

        every { userServiceMock.addUser(any()) } returns userDTO(id = 1)

        val response = webTestClient
            .post()
            .uri("/v1/users")
            .bodyValue(userDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals("userDTO.email must not be blank, userDTO.fullName must not be blank", response)

    }

    @Test
    fun addUser_runtimeException(){
        val userDTO = UserDTO(null, "Test User", "test@gmail.com", "iamatest", null, null, null)

        val errorMessage = "Unexpected Error occurred"
        every { userServiceMock.addUser(any()) } throws RuntimeException(errorMessage)

        val response = webTestClient
            .post()
            .uri("/v1/users")
            .bodyValue(userDTO)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(errorMessage, response)

    }

    @Test
    fun retrieveUser(){
        every { userServiceMock.retrieveUser("test@gmail.com","iamatest") }.returns(
            userDTO(id = 1, email = "test@gmail.com", password = "iamatest")
        )

        val userDTO = webTestClient
            .get()
            .uri("/v1/users?email=test@gmail.com&password=iamatest")
            .exchange()
            .expectStatus().isOk
            .expectBody(UserDTO::class.java)
            .returnResult()
            .responseBody

        println("userDTO: $userDTO")
        assertEquals("test@gmail.com", userDTO!!.email)
        assertEquals("iamatest", userDTO!!.password)
    }

    @Test
    fun updateProfilePicture(){
        every { userServiceMock.updateProfilePicture(any()) } just runs

        val imageDTO = ImageDTO(
            "2",
            "https://firebasestorage.googleapis.com/v0/b/imageapp-e6c43.appspot.com/o/images%2Fandroid.graphics.Bitmap%40f0efa6e?alt=media&token=30fb108c-f31f-4b5d-830e-f3791e0d4aa9"
        )

        webTestClient
            .post()
            .uri("/v1/users/image")
            .bodyValue(imageDTO)
            .exchange()
            .expectStatus().isOk

    }

    @Test
    fun updateProfilePicture_validation(){
        every { userServiceMock.updateProfilePicture(any()) } just runs

        val imageDTO = ImageDTO(
            "",
            ""
        )

        val response = webTestClient
            .post()
            .uri("/v1/users/image")
            .bodyValue(imageDTO)
            .exchange()
            .expectStatus().isBadRequest
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals("imageDTO.profilePicture must not be blank, imageDTO.userId must not be blank", response)
    }
}