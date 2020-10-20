package com.mooc.ppjoke

import android.os.Bundle
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.Navigation
import androidx.navigation.ui.NavigationUI
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.mooc.ppjoke.model.Destination
import com.mooc.ppjoke.model.User
import com.mooc.ppjoke.ui.login.UserManager
import com.mooc.ppjoke.utils.AppConfig.Companion.getDestConfig
import com.mooc.ppjoke.utils.NavGraphBuilder
import com.mooc.ppjoke.utils.StatusBar


class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.AppTheme)
        StatusBar.fitSystemBar(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView: BottomNavigationView = findViewById(R.id.nav_view)

        val navController = Navigation.findNavController(this, R.id.nav_host_fragment)

        // 将bottomView和navController关联
        NavigationUI.setupWithNavController(navView, navController)

        // 将navGraph添加到navController中
        NavGraphBuilder.build(navController)

        // 将bottomView的点击事件和NavController的跳转相关联
        navView.setOnNavigationItemSelectedListener {

            val menuItemId = it.itemId
            val destConfig: HashMap<String, Destination> = getDestConfig()
            val iterator: Iterator<Map.Entry<String, Destination>> = destConfig.entries.iterator()
            //遍历 target destination 是否需要登录拦截
            while (iterator.hasNext()) {
                val entry: Map.Entry<String, Destination> = iterator.next()
                val value: Destination = entry.value
                if (value != null && !UserManager.isLogin() && value.needLogin && value.id == menuItemId
                ) {
                    UserManager.login(this)
                        .observe(this, Observer<User> { navView.selectedItemId = menuItemId })
                    return@setOnNavigationItemSelectedListener false
                }
            }

            navController.navigate(it.itemId)
            !TextUtils.isEmpty(it.title)
        }
    }
}