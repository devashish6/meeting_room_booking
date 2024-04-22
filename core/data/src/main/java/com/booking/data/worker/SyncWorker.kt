package com.booking.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.booking.data.model.asEntity
import com.booking.data.repository.DataRepository
import com.booking.database.com.booking.database.repository.LocalDataSource
import com.booking.network.retrofit.RemoteDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject

@HiltWorker
class SyncWorker @AssistedInject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val dataRepository: DataRepository,
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {

    private val TAG = "DATA_MODULE_DEBUG"
    override suspend fun doWork(): Result {
        Log.d(TAG, localDataSource.toString())
        Log.d(TAG, remoteDataSource.toString())
        val syncedSuccessfully: Boolean = dataRepository.syncWith()
        return if (syncedSuccessfully) {
            Log.d(TAG, "Success")
            Result.success()
        } else {
            Log.d(TAG, "Retry")
            Result.retry()
        }
    }
}