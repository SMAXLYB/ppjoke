package com.mooc.ppjoke.ui.sofa

import android.R
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import androidx.viewpager2.widget.ViewPager2.OnPageChangeCallback
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.mooc.libnavannotation.FragmentDestination
import com.mooc.ppjoke.databinding.FragmentSofaBinding
import com.mooc.ppjoke.model.SofaTab
import com.mooc.ppjoke.model.SofaTab.Tabs
import com.mooc.ppjoke.ui.home.HomeFragment
import com.mooc.ppjoke.utils.AppConfig


@FragmentDestination(pageUrl = "main/tabs/sofa")
class SofaFragment : Fragment() {
    private lateinit var binding: FragmentSofaBinding
    protected lateinit var viewPager2: ViewPager2
    protected lateinit var tabLayout: TabLayout
    private var tabConfig: SofaTab? = null
    private val tabs: ArrayList<Tabs> = arrayListOf()
    private var mediator: TabLayoutMediator? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentSofaBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        viewPager2 = binding.viewPager
        tabLayout = binding.tabLayout

        tabConfig = getTabConfig()
        //将开启的tab添加进来
        for (tab in tabConfig!!.tabs) {
            if (tab.enable) {

                tabs.add(tab)
            }
        }

        //限制页面预加载
        viewPager2.offscreenPageLimit = ViewPager2.OFFSCREEN_PAGE_LIMIT_DEFAULT
        viewPager2.adapter = object : FragmentStateAdapter(childFragmentManager, this.lifecycle) {

            override fun createFragment(position: Int): Fragment {
                return getTabFragment(position)
            }

            override fun getItemCount(): Int {
                return tabs.size
            }
        }

        // 关联tabLayout和ViewPager
        mediator = TabLayoutMediator(
            tabLayout, viewPager2, true
        ) { tab, position -> tab.customView = makeTabView(position) }
        mediator!!.attach()

        // 对viewpager进行监听,改变tabLayout的文字大小
        viewPager2.registerOnPageChangeCallback(mPageChangeCallback);

        // 延迟设置默认选中项
        viewPager2.post {
            viewPager2.setCurrentItem(
                tabConfig!!.select,
                false
            )
        }
    }

    private var mPageChangeCallback: OnPageChangeCallback = object : OnPageChangeCallback() {
        override fun onPageSelected(position: Int) {
            val tabCount = tabLayout.tabCount
            for (i in 0 until tabCount) {
                val tab = tabLayout.getTabAt(i)
                val customView = tab!!.customView as TextView
                if (tab.position == position) {
                    customView.textSize = tabConfig!!.activeSize.toFloat()
                    customView.typeface = Typeface.DEFAULT_BOLD
                } else {
                    customView.textSize = tabConfig!!.normalSize.toFloat()
                    customView.typeface = Typeface.DEFAULT
                }
            }
        }
    }

    private fun makeTabView(position: Int): View {
        val tabView = TextView(context)
        val states = arrayOfNulls<IntArray>(2)
        states[0] = intArrayOf(R.attr.state_selected)
        states[1] = intArrayOf()
        val colors = intArrayOf(
            Color.parseColor(tabConfig!!.activeColor), Color.parseColor(
                tabConfig!!.normalColor
            )
        )
        val stateList = ColorStateList(states, colors)
        tabView.setTextColor(stateList)
        tabView.text = tabs[position].title
        tabView.textSize = tabConfig!!.normalSize.toFloat()
        return tabView
    }

    private fun getTabConfig(): SofaTab {
        return AppConfig.getSofaTabConfig()
    }

    fun getTabFragment(position: Int): Fragment {
        return HomeFragment.newInstance(tabs[position].tag)
    }

    override fun onDestroy() {
        mediator!!.detach()
        viewPager2.unregisterOnPageChangeCallback(mPageChangeCallback)
        super.onDestroy()
    }
}