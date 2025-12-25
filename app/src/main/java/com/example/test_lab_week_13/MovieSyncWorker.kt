package com.example.test_lab_week_13

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.test_lab_week_13.model.MovieDatabase

class MovieSyncWorker(
    context: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val app = applicationContext as MovieApplication
            app.movieRepository.refreshMovies()
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }
}
