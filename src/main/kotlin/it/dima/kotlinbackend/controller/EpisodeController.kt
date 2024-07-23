package it.dima.kotlinbackend.controller

import it.dima.kotlinbackend.dto.EpisodeDTO
import it.dima.kotlinbackend.service.EpisodeService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/episode")
class EpisodeController(val episodeService: EpisodeService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addWatchedEpisode(@RequestBody @Valid episodeDTO: EpisodeDTO): EpisodeDTO{
        return episodeService.addWatchedEpisode(episodeDTO)
    }

    @GetMapping
    fun getWatchedEpisodes():List<EpisodeDTO>{
        return emptyList()
    }

    @DeleteMapping
    fun deleteWatchedEpisode(@RequestBody @Valid episodeDTO: EpisodeDTO){}

}