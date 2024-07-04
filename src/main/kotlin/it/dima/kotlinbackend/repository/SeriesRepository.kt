package it.dima.kotlinbackend.repository


import it.dima.kotlinbackend.entity.Series
import org.springframework.data.repository.CrudRepository
import java.util.*

interface SeriesRepository: CrudRepository<Series, Long>{
}