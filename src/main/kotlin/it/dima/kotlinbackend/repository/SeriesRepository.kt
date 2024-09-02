package it.dima.kotlinbackend.repository


import it.dima.kotlinbackend.entity.Series
import org.springframework.data.repository.CrudRepository

interface SeriesRepository: CrudRepository<Series, Long>{
}