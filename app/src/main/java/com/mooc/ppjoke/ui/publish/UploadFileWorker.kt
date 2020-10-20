package com.mooc.ppjoke.ui.publish

import android.content.Context
import androidx.work.Worker
import androidx.work.WorkerParameters

class UploadFileWorker(context: Context, params: WorkerParameters) : Worker(context, params) {

    override fun doWork(): Result {
        return Result.success()
    }
}