package com.booking.slots

import android.app.Application
import androidx.work.Configuration
import com.booking.data.worker.SyncWorkerFactory
import com.booking.data.worker.initializeWorker
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class SlotsApplication : Application(), Configuration.Provider {

    @Inject
    lateinit var hiltWorkerFactory: SyncWorkerFactory
    override fun onCreate() {
        super.onCreate()
        initializeWorker(this)
    }

    override val workManagerConfiguration: Configuration
        get() = Configuration.Builder()
            .setWorkerFactory(hiltWorkerFactory)
            .build()
}