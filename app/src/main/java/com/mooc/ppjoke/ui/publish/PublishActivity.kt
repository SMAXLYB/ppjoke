package com.mooc.ppjoke.ui.publish

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.observe
import androidx.work.*
import com.mooc.libnavannotation.ActivityDestination
import com.mooc.ppjoke.R
import com.mooc.ppjoke.databinding.ActivityPublishBinding
import com.mooc.ppjoke.model.TagList
import com.mooc.ppjoke.utils.StatusBar
import java.util.concurrent.TimeUnit


@ActivityDestination(pageUrl = "main/tabs/publish", needLogin = false)
class PublishActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var mBinding: ActivityPublishBinding
    private var mTagList: TagList? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        StatusBar.fitSystemBar(this)
        super.onCreate(savedInstanceState)

        mBinding =
            DataBindingUtil.setContentView<ActivityPublishBinding>(this, R.layout.activity_publish)

        mBinding.actionClose.setOnClickListener(this);
        mBinding.actionPublish.setOnClickListener(this);
        mBinding.actionDeleteFile.setOnClickListener(this);
        mBinding.actionAddTag.setOnClickListener(this);
        mBinding.actionAddFile.setOnClickListener(this);
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.action_close -> showExitDialog()
            R.id.action_publish -> publish()
            R.id.action_add_tag -> {
                val fragment = TagBottomSheetDialog()
                fragment.setOnTagItemSelectedListener(object :
                    TagBottomSheetDialog.OnTagItemSelectedListener {
                    override fun onTagItemSelected(tagList: TagList) {
                        mTagList = tagList
                        mBinding.actionAddTag.text = tagList.title
                    }
                })
                fragment.show(supportFragmentManager, "tag_dialog")
            }
        }
    }

    @SuppressLint("RestrictedApi")
    private fun publish() {
        val inputData = Data.Builder()
            .putString("file", "test")
            .build()

        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val workRequest = OneTimeWorkRequest.Builder(UploadFileWorker::class.java)
            .setInputData(inputData)
            .setConstraints(constraints)
            .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 10, TimeUnit.SECONDS)
            .setInitialDelay(10, TimeUnit.SECONDS)
            .setInitialRunAttemptCount(2)
            .setPeriodStartTime(0, TimeUnit.SECONDS)
            .build()

        val id = workRequest.id

        WorkManager.getInstance(this).getWorkInfoByIdLiveData(id).observe(this) {
            if (it != null && it.state == WorkInfo.State.SUCCEEDED) {
                Toast.makeText(this, "发布成功", Toast.LENGTH_SHORT).show()
                this.finish()
            }
        }

    }

    private fun showExitDialog() {
        AlertDialog.Builder(this)
            .setMessage(getString(R.string.publish_exit_message))
            .setNegativeButton(getString(R.string.publish_exit_action_cancel), null)
            .setPositiveButton(getString(R.string.publish_exit_action_ok),
                DialogInterface.OnClickListener { dialog, _ ->
                    dialog.dismiss()
                    finish()
                }).create().show()
    }
}