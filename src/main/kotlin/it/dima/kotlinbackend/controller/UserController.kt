package it.dima.kotlinbackend.controller

import it.dima.kotlinbackend.dto.ImageDTO
import it.dima.kotlinbackend.dto.UserDTO
import it.dima.kotlinbackend.service.UserService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/v1/users")
@Validated
class UserController(val userService: UserService) {
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun addUser(@RequestBody @Valid userDTO: UserDTO): UserDTO {
        return userService.addUser(userDTO)
    }

    @GetMapping
    fun retrieveUser(
        @RequestParam("email", required = true) email: String,
        @RequestParam("password", required = true) password: String
    ): UserDTO = userService.retrieveUser(email, password)


    @GetMapping("image")
    fun retrieveProfilePicture(
        @RequestParam("user_id", required = true) userId: Long
    ): String? = userService.retrieveProfilePicture(userId)

    @PostMapping("image")
    @ResponseStatus(HttpStatus.OK)
    fun updateProfilePicture(@RequestBody @Valid imageDTO: ImageDTO){
        return userService.updateProfilePicture(imageDTO)
    }



}