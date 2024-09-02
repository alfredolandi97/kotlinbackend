package it.dima.kotlinbackend.repository

import it.dima.kotlinbackend.entity.User
import it.dima.kotlinbackend.utils.PostgresSQLContainerInitializer
import it.dima.kotlinbackend.utils.episodeEntityList
import it.dima.kotlinbackend.utils.seriesEntityList
import it.dima.kotlinbackend.utils.userEntityList
import org.junit.jupiter.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.data.jpa.repository.Query
import org.springframework.test.context.ActiveProfiles
import java.util.*
import java.util.stream.Stream

@DataJpaTest
@ActiveProfiles("test")
@AutoConfigureTestDatabase(replace= AutoConfigureTestDatabase.Replace.NONE)
class EpisodeRepositoryIntgTest: PostgresSQLContainerInitializer() {
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
        val episodes = episodeEntityList(users, series)
        episodeRepository.saveAll(episodes)
    }

    @Test
    fun findEpisodeByCompositeKey(){
        val episode = episodeRepository.findEpisodeByCompositeKey(47145,5,2)
        println("Episode: $episode")

        Assertions.assertEquals(47145, episode!!.get().id.seriesId)
        Assertions.assertEquals(5, episode!!.get().id.season)
        Assertions.assertEquals(2, episode!!.get().id.episode)
    }
}