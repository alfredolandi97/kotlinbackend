package it.dima.kotlinbackend.controller

import it.dima.kotlinbackend.dto.FollowDTO
import it.dima.kotlinbackend.service.FavoritesService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/favorites")
@Validated
class FavoritesController(val favoritesService: FavoritesService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addCourse(@RequestBody @Valid followDTO: FollowDTO): FollowDTO {
        return favoritesService.addFavoriteSeries(followDTO)
    }

    @DeleteMapping
    @ResponseStatus(HttpStatus.OK)
    fun deleteFavorite(@RequestParam("user_id", required = true) userId: Long, @RequestParam("series_id", required = true) seriesId: Long) = favoritesService.deleteFavorite(userId, seriesId)

    @GetMapping
    fun retrieveAllFavoritesByUserId(@RequestParam("user_id", required = true) userId: Long): List<Long> = favoritesService.retrieveAllFavoritesByUserId(userId)

}