package com.booking.data.worker

import android.content.Context
import androidx.work.Constraints
import androidx.work.ExistingWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.OutOfQuotaPolicy
import androidx.work.WorkManager

fun initializeWorker(context: Context) {
    WorkManager.getInstance(context)
        .enqueueUniqueWork(
        "SyncWorkName",
        ExistingWorkPolicy.KEEP,
        workerRequest(),
    )

}

fun workerRequest (): OneTimeWorkRequest {
    return OneTimeWorkRequestBuilder<SyncWorker>()
        .setExpedited(OutOfQuotaPolicy.RUN_AS_NON_EXPEDITED_WORK_REQUEST)
        .setConstraints(SyncConstraints)
        .build()
}

val SyncConstraints
    get() = Constraints.Builder()
        .setRequiredNetworkType(NetworkType.CONNECTED)
        .build()