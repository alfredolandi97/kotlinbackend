package it.dima.kotlinbackend.controller

import it.dima.kotlinbackend.dto.ImageDTO
import it.dima.kotlinbackend.dto.UserDTO
import it.dima.kotlinbackend.entity.User
import it.dima.kotlinbackend.exception.InvalidSubscriptionException
import it.dima.kotlinbackend.exception.UserNotFoundException
import it.dima.kotlinbackend.repository.UserRepository
import it.dima.kotlinbackend.utils.userEntityList
import org.junit.jupiter.api.Assertions
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
class UserControllerIntgTest {
    @Autowired
    lateinit var webTestClient: WebTestClient

    @Autowired
    lateinit var userRepository: UserRepository

    @BeforeEach
    fun setUp() {
        userRepository.deleteAll()

        val users = userEntityList()
        userRepository.saveAll(users)
    }

    @Test
    fun addSubscribedUserTest(){
        val userDTO = UserDTO(null, "Test User", "test@gmail.com", "iamatest", null, null, null)

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
    fun addSubscribedUserTest_nullPassword(){
        val userDTO = UserDTO(null, "Test User", "test@gmail.com", null, null, null, null)

        val response = webTestClient
            .post()
            .uri("/v1/users")
            .bodyValue(userDTO)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(
            InvalidSubscriptionException("Password cannot be unspecified").toString(),
            InvalidSubscriptionException(response!!)
                .toString()
        )
    }

    @Test
    fun addSubscribedUserTest_emptyPassword(){
        val userDTO = UserDTO(null, "Test User", "test@gmail.com", "", null, null, null)

        val response = webTestClient
            .post()
            .uri("/v1/users")
            .bodyValue(userDTO)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(
            InvalidSubscriptionException("Invalid subscription fields").toString(),
            InvalidSubscriptionException(response!!)
                .toString()
        )
    }

    @Test
    fun addMetaApiUserTest(){
        val userDTO = UserDTO(null, "Test API User", "test.api@gmail.com", null, "eivneinownvono43i3ir4", null, null)

        val metaApiUserDTO = webTestClient
            .post()
            .uri("/v1/users")
            .bodyValue(userDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(UserDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue{
            metaApiUserDTO!!.id != null
        }
    }

    @Test
    fun addGoogleApiUserTest(){
        val userDTO = UserDTO(null, "Test API User", "test.api@gmail.com", null, null, "eivneinownvono43i3ir4", null)

        val googleApiUserDTO = webTestClient
            .post()
            .uri("/v1/users")
            .bodyValue(userDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(UserDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue{
            googleApiUserDTO!!.id != null
        }
    }

    @Test
    fun addApiUserTest_alreadyAuthenticated(){
        val userDTO = UserDTO(null,
            "Federico Lamperti",
            "fedelampe11@gmail.com",
            null,
            null,
            "qAuhHd4EssQHTdEh1zJE1Qdtl2p1",
            null
        )

        val apiAuthUserDTO = webTestClient
            .post()
            .uri("/v1/users")
            .bodyValue(userDTO)
            .exchange()
            .expectStatus().isCreated
            .expectBody(UserDTO::class.java)
            .returnResult()
            .responseBody

        Assertions.assertTrue{
            apiAuthUserDTO!!.id != null
        }
    }

    @Test
    fun addUserTest_emailAlreadyTaken(){
        val userDTO = UserDTO(null, "Test User", "alfredo.landi@mail.polimi.it", "iamatest", null, null, null)

        val response = webTestClient
            .post()
            .uri("/v1/users")
            .bodyValue(userDTO)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(
            InvalidSubscriptionException("Email already taken").toString(),
            InvalidSubscriptionException(response!!)
                .toString()
        )
    }

    @Test
    fun retrieveUserTest(){
        val email = "alfredo.landi@mail.polimi.it"
        val password = "landi"

        val userDTO = webTestClient
            .get()
            .uri("/v1/users?email={email}&password={password}", email, password)
            .exchange()
            .expectStatus().isOk
            .expectBody(UserDTO::class.java)
            .returnResult()
            .responseBody

        println("userDTO: $userDTO")
        assertEquals(email, userDTO!!.email)
        assertEquals(password, userDTO!!.password)
    }

    @Test
    fun retrieveUserTest_invalidCredentials(){
        val email = "not.user@mail.polimi.it"
        val password = "error"

        val response = webTestClient
            .get()
            .uri("/v1/users?email={email}&password={password}", email, password)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(
            UserNotFoundException(
                "User not found for credentials email: $email, password: $password"
            ).toString(),
            UserNotFoundException(response!!)
                .toString()
        )
    }



    @Test
    fun updateProfilePictureTest(){
        val userDTO = UserDTO(null, "Test User Image", "test.image@gmail.com", "iamatestimage", null, null, null)
        val userEntity = userDTO.let {
            User(
                null,
                it.full_name,
                it.email,
                it.password,
                it.meta_api_key,
                it.google_api_key,
                it.profile_picture
            )
        }
        userRepository.save(userEntity)

        val imageDTO = ImageDTO(
            userEntity.id.toString(),
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
    fun updateProfilePictureTest_invalidUserId(){
        val testId: Long = -1

        val imageDTO = ImageDTO(
            testId.toString(),
            "https://firebasestorage.googleapis.com/v0/b/imageapp-e6c43.appspot.com/o/images%2Fandroid.graphics.Bitmap%40f0efa6e?alt=media&token=30fb108c-f31f-4b5d-830e-f3791e0d4aa9"
        )

        val response = webTestClient
            .post()
            .uri("/v1/users/image")
            .bodyValue(imageDTO)
            .exchange()
            .expectStatus().is5xxServerError
            .expectBody(String::class.java)
            .returnResult()
            .responseBody

        assertEquals(
            UserNotFoundException(
                "User not found"
            ).toString(),
            UserNotFoundException(response!!)
                .toString()
        )

    }
}