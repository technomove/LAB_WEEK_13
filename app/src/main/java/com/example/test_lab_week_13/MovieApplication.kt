package com.example.test_lab_week_12

import android.app.Application
import com.example.test_lab_week_12.api.MovieService
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.util.concurrent.TimeUnit

class MovieApplication : Application() {

    lateinit var movieRepository: MovieRepository

    override fun onCreate() {
        super.onCreate()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.themoviedb.org/3/")
            .addConverterFactory(MoshiConverterFactory.create())
            .build()

        val movieService = retrofit.create(MovieService::class.java)
        movieRepository = MovieRepository(movieService)

        val workRequest =
            PeriodicWorkRequestBuilder<MovieSyncWorker>(
                1, TimeUnit.HOURS
            ).build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "movie_sync_work",
            ExistingPeriodicWorkPolicy.KEEP,
            workRequest
        )
    }
}
