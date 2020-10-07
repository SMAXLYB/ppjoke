package com.mooc.ppjoke

import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.Navigation
import androidx.navigation.Navigation.findNavController
import androidx.navigation.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mooc.ppjoke.utils.NavGraphBuilder


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        // val navController = findNavController(R.id.nav_host_fragment)
        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        // appBar与navController关联
//        val appBarConfiguration = AppBarConfiguration(setOf(
//                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_notifications))
//        setupActionBarWithNavController(navController, appBarConfiguration)

        // 将bottomView和navController关联
        // navView.setupWithNavController(navController)
        NavigationUI.setupWithNavController(navView,navController)

        // 将navGraph添加到navController中
        NavGraphBuilder.build(navController)

        // 将bottomView的点击事件和NavController的跳转相关联
        navView.setOnNavigationItemSelectedListener {
            navController.navigate(it.itemId)
            !TextUtils.isEmpty(it.title)
        }
    }
}