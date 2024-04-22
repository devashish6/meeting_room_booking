package com.booking.data.worker

import android.content.Context
import android.util.Log
import androidx.hilt.work.HiltWorker
import androidx.tracing.traceAsync
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.booking.data.model.asEntity
import com.booking.database.com.booking.database.repository.LocalDataSource
import com.booking.database.com.booking.database.repository.LocalDataSourceInterface
import com.booking.network.retrofit.RemoteDataSource
import dagger.assisted.Assisted
import dagger.assisted.AssistedInject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

@HiltWorker
class SyncWorker @AssistedInject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    @Assisted private val context: Context,
    @Assisted workerParameters: WorkerParameters,
) : CoroutineWorker(context, workerParameters) {

    private val TAG = "DATA_MODULE_DEBUG"
    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
        traceAsync("Sync", 0) {
            Log.d(TAG, "HELLO w02w20w2w0")
            Log.d(TAG, localDataSource.toString())
            Log.d(TAG, remoteDataSource.toString())
            Log.d(TAG, remoteDataSource.getAllMeetingRooms().body()!!.documents[0].fields.asEntity().toString())
            Result.success()
        }
    }
}