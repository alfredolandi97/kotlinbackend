package it.dima.kotlinbackend.service

import it.dima.kotlinbackend.dto.SeriesDTO
import java.util.*
import it.dima.kotlinbackend.entity.Series
import it.dima.kotlinbackend.repository.SeriesRepository
import it.dima.kotlinbackend.utils.SeriesArrayReply
import it.dima.kotlinbackend.utils.SeriesDetailsReply
import mu.KLogging
import org.springframework.boot.web.client.RestTemplateBuilder
import org.springframework.stereotype.Service

@Service
class SeriesService(val seriesRepository: SeriesRepository) {


    val restTemplate = RestTemplateBuilder().build()
    companion object: KLogging()

    fun getSeriesById(seriesId: Long): SeriesDTO {

        val reply: SeriesDetailsReply = restTemplate.getForObject(
            "https://www.episodate.com/api/show-details?q=$seriesId", SeriesDetailsReply::class.java
        )!!

        return reply.let {
            SeriesDTO(
                it.tvShow.id,
                it.tvShow.name,
                it.tvShow.description,
                it.tvShow.start_date,
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

    fun getMostPopular(): List<SeriesDTO> {

        val reply: SeriesArrayReply = restTemplate.getForObject(
            "https://www.episodate.com/api/most-popular?page=1", SeriesArrayReply::class.java
        )!!

        return reply.tv_shows.map {
            SeriesDTO(
                it.id,
                it.name,
                null,
                it.start_date,
                it.status,
                it.network,
                it.image_thumbnail_path,
                null,
                null,
                null,
                null
            )
        }
    }

    fun getSeriesByQuery(query: String): List<SeriesDTO> {

        val reply: SeriesArrayReply = restTemplate.getForObject(
            "https://www.episodate.com/api/search?q=$query", SeriesArrayReply::class.java
        )!!

        return reply.tv_shows.map {
            SeriesDTO(
                it.id,
                it.name,
                null,
                it.start_date,
                it.status,
                it.network,
                it.image_thumbnail_path,
                null,
                null,
                null,
                null
            )
        }
    }

    fun findSeriesById(seriesId: Long): Optional<Series> {
        return seriesRepository.findById(seriesId)
    }
}