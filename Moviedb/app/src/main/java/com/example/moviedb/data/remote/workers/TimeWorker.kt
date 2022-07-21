package com.example.moviedb.data.remote.workers

import android.content.ContentValues.TAG
import android.content.Context
import android.util.Log
import androidx.work.*
import com.example.moviedb.utils.PermissionUtils.getLocation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.util.concurrent.TimeUnit

class TimeWorkerLocation(private val ctx: Context, params: WorkerParameters) :
    CoroutineWorker(ctx, params) {
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            try {
                getLocation(ctx)
                Result.success()
            } catch (e: Exception) {
                Log.d("$TAG", "Exception getting location -->  ${e.message}")
                Result.failure()
            }
        }
    }

    companion object {
        private const val TAG = "UpdateLocationWorker"
        private const val DEFAULT_MIN_INTERVAL = 15L

        @JvmStatic
        fun schedule() {
            val worker = PeriodicWorkRequestBuilder<TimeWorkerLocation>(DEFAULT_MIN_INTERVAL, TimeUnit.MINUTES)
                .addTag(TAG).build()
            WorkManager.getInstance() // provide application instance here
                .enqueueUniquePeriodicWork(TAG, ExistingPeriodicWorkPolicy.REPLACE, worker)
        }
    }
}