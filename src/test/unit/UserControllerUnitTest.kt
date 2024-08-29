import com.ninjasquad.springmockk.MockkBean
import io.mockk.every
import it.dima.kotlinbackend.controller.UserController
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
}