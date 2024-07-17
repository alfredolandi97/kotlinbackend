package it.dima.kotlinbackend.service

import it.dima.kotlinbackend.dto.SeriesDTO
import it.dima.kotlinbackend.dto.UserDTO
import it.dima.kotlinbackend.utils.SeriesDetailsReply
import mu.KLogging
import org.springframework.boot.CommandLineRunner
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Profile
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate

@Service
class SeriesService {

    companion object: KLogging()

    fun getSeriesById(seriesId: Long): SeriesDTO {
        val restTemplate = RestTemplateBuilder().build()

        val reply: SeriesDetailsReply = restTemplate.getForObject(
            "https://www.episodate.com/api/show-details?q=$seriesId", SeriesDetailsReply::class.java
        )!!

        return reply.let {
            SeriesDTO(
                it.tvShow.id,
                it.tvShow.name,
                it.tvShow.description,
                it.tvShow.status,
                it.tvShow.network,
                it.tvShow.image_thumbnail_path,
                it.tvShow.rating,
                it.tvShow.genres,
                it.tvShow.countdown,
                it.tvShow.episodes
            )
        }

    }
}