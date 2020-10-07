package com.mooc.ppjoke.ui.publish

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.mooc.libnavannotation.ActivityDestination
import com.mooc.libnavannotation.FragmentDestination
import com.mooc.ppjoke.R

@ActivityDestination(pageUrl = "main/tabs/publish",needLogin = true)
class PublishActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_publish)
    }
}