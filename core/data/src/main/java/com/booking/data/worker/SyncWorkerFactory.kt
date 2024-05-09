package com.booking.data.worker

import android.content.Context
import androidx.work.ListenableWorker
import androidx.work.WorkerFactory
import androidx.work.WorkerParameters
import com.booking.data.repository.DataRepository
import com.booking.database.com.booking.database.repository.LocalDataSource
import com.booking.network.retrofit.RemoteDataSource
import javax.inject.Inject

class SyncWorkerFactory @Inject constructor(
    private val remoteDataSource: RemoteDataSource,
    private val localDataSource: LocalDataSource,
    private val dataRepository: DataRepository
) : WorkerFactory() {
    override fun createWorker(
        appContext: Context,
        workerClassName: String,
        workerParameters: WorkerParameters
    ): ListenableWorker = SyncWorker(
        remoteDataSource = remoteDataSource,
        localDataSource = localDataSource,
        dataRepository = dataRepository,
        context =  appContext,
        workerParameters = workerParameters)

}