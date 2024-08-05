package it.dima.kotlinbackend.controller

import it.dima.kotlinbackend.dto.SeriesDTO
import it.dima.kotlinbackend.service.SeriesService
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/v1/series")
@Validated
class SeriesController(val seriesService: SeriesService) {
    @GetMapping
    fun getSeriesById(@RequestParam("series_id", required = true) seriesId: Long): SeriesDTO = seriesService.getSeriesById(seriesId)

    @GetMapping("most_popular")
    fun getMostPopular(): List<SeriesDTO> = seriesService.getMostPopular()

    @GetMapping("search")
    fun getSeriesByQuery(@RequestParam("q", required = true) query: String): List<SeriesDTO> = seriesService.getSeriesByQuery(query)
}