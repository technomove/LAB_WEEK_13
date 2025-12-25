package com.example.test_lab_week_13

import com.example.test_lab_week_13.api.MovieService
import com.example.test_lab_week_13.model.Movie
import com.example.test_lab_week_13.model.MovieDao
import com.example.test_lab_week_13.model.MovieEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext

class MovieRepository(
    private val movieService: MovieService,
    private val movieDao: MovieDao
) {

    private val apiKey = "e82143fb71513dfd162f253570813082"

    fun getMovies(): Flow<List<Movie>> {
        return movieDao.getMovies().map { entities ->
            entities.map { entity ->
                Movie(
                    id = entity.id,
                    title = entity.title,
                    overview = entity.overview,
                    posterPath = entity.posterPath,
                    releaseDate = entity.releaseDate,
                    popularity = entity.popularity
                )
            }
        }
    }

    suspend fun refreshMovies() {
        withContext(Dispatchers.IO) {
            val movies = movieService.getPopularMovies(apiKey).results

            val entities = movies.map { movie ->
                MovieEntity(
                    id = movie.id,
                    title = movie.title,
                    overview = movie.overview,
                    posterPath = movie.posterPath,
                    releaseDate = movie.releaseDate,
                    popularity = movie.popularity
                )
            }

            movieDao.deleteAll()
            movieDao.insertMovies(entities)
        }
    }
}
