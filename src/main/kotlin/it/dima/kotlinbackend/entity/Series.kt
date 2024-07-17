package it.dima.kotlinbackend.entity

import jakarta.persistence.*

@Entity
data class Series(
    @Id
    @Column(name = "series_id")
    val seriesId: Long,

    @ManyToMany(mappedBy = "series")
    val users: MutableList<User> = mutableListOf(),
){
    override fun toString(): String {
        var usersString: String = ""
        for(u in users){
            usersString = usersString + "," + u.id.toString()
        }
        return "Series(seriesId='$seriesId', users='$usersString')"
    }
}