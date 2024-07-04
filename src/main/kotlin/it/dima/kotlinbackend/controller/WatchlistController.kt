/*package it.dima.kotlinbackend.controller

import it.dima.kotlinbackend.dto.SeenDTO
import it.dima.kotlinbackend.service.WatchListService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/watchlist")
@Validated
class WatchlistController(val watchListService: WatchListService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addCourse(@RequestBody @Valid seenDTO: SeenDTO): SeenDTO{
        return watchListService.addWatchedEpisode(seenDTO)
    }

    @GetMapping
    fun getFavoritesByUserId(
        @RequestParam("user_id", required = true) userId: String
    ):List<SeenDTO> = watchListService.retrieveWatchedEpisodesByUserId(userId)
}*/