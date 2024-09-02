package it.dima.kotlinbackend.repository

import it.dima.kotlinbackend.utils.PostgresSQLContainerInitializer
import it.dima.kotlinbackend.utils.episodeEntityList
import it.dima.kotlinbackend.utils.seriesEntityList
import it.dima.kotlinbackend.utils.userEntityList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.test.context.ActiveProfiles

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class RepositoriesIntgTest: PostgresSQLContainerInitializer() {
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
        //val episodes = episodeEntityList(users, series)
        //episodeRepository.saveAll(episodes)
    }

    @Test
    fun findByEmailAndPasswordContaining(){
        val user = userRepository.findByEmailAndPasswordContaining("alfredo.landi@mail.polimi.it", "landi")
        println("User: $user")

        Assertions.assertEquals("alfredo.landi@mail.polimi.it", user!!.get().email)
        Assertions.assertEquals("landi", user!!.get().password)
    }

    @Test
    fun findByMetaApiKeyContaining(){
        val user = userRepository.findByMetaApiKeyContaining("USyvXZWcBuX0zGtk3lCFkUthVW83")
        println("User: $user")

        Assertions.assertEquals("USyvXZWcBuX0zGtk3lCFkUthVW83", user!!.get().meta_api_key)
    }

    @Test
    fun findByGoogleApiKeyContaining() {
        val user = userRepository.findByGoogleApiKeyContaining("qAuhHd4EssQHTdEh1zJE1Qdtl2p1")
        println("User: $user")

        Assertions.assertEquals("qAuhHd4EssQHTdEh1zJE1Qdtl2p1", user!!.get().google_api_key)
    }

    @Test
    fun findByEmailContaining(){
        val user = userRepository.findByEmailContaining("federico.lamperti@mail.polimi.it")
        println("User: $user")

        Assertions.assertEquals("federico.lamperti@mail.polimi.it", user!!.get().email)
    }

    /*@Test
    fun findEpisodeByCompositeKey(){
        val episode = episodeRepository.findEpisodeByCompositeKey(47145,5,2)
        println("Episode: $episode")

        Assertions.assertEquals(47145, episode!!.get().id.seriesId)
        Assertions.assertEquals(5, episode!!.get().id.season)
        Assertions.assertEquals(2, episode!!.get().id.episode)
    }*/
}