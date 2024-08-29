package it.dima.kotlinbackend.controller


import it.dima.kotlinbackend.dto.EpisodeDTO
import it.dima.kotlinbackend.dto.WatchedDTO
import it.dima.kotlinbackend.service.WatchedService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/watched")
@Validated
class WatchedController(val watchedService: WatchedService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addWatchedEpisode(@RequestBody @Valid watchedDTO: WatchedDTO): WatchedDTO {
        return watchedService.addWatchedEpisode(watchedDTO)
    }

    @GetMapping
    fun getWatchedEpisodesBySeries(
        @RequestParam("user_id", required = true) userId: Long,
        @RequestParam("series_id", required = true) seriesId: Long)
    :List<EpisodeDTO> = watchedService.getWatchedEpisodesBySeries(userId, seriesId)

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    fun deleteWatchedEpisode(
        @RequestParam("user_id", required = true) userId: Long,
        @RequestParam("series_id", required = true) seriesId: Long,
        @RequestParam("season", required = true) season: Int,
        @RequestParam("episode", required = true) episode: Int,
    )
        = watchedService.deleteWatchedService(userId, seriesId, season, episode)
}