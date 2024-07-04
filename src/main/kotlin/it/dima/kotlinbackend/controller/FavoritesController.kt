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

    //TO-DO
    //DELETE

    //TO-DO
    //GET

}