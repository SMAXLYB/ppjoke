package com.mooc.ppjoke.ui.sofa

import android.os.Bundle
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mooc.libnavannotation.FragmentDestination
import com.mooc.ppjoke.R
import org.w3c.dom.Text

@FragmentDestination(pageUrl = "main/tabs/sofa")
class SofaFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sofa, container, false)
    }
}