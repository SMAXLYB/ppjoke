package com.mooc.ppjoke.utils

import android.content.ComponentName
import androidx.navigation.ActivityNavigator
import androidx.navigation.NavController
import androidx.navigation.NavGraph
import androidx.navigation.NavGraphNavigator
import androidx.navigation.fragment.FragmentNavigator
import com.mooc.libcommon.AppGlobals

class NavGraphBuilder {

    companion object {
        @JvmStatic
        fun build(controller: NavController) {
            // 获取创建节点的navigator
            val provider = controller.navigatorProvider
            val fragmentNavigator = provider.getNavigator(FragmentNavigator::class.java)
            val activityNavigator = provider.getNavigator(ActivityNavigator::class.java)

            // 创建navGraph对象用来存放所有节点信息
            val navGraph = NavGraph(NavGraphNavigator(provider))

            // 获取节点的信息
            val destConfig = AppConfig.getDestConfig()
            destConfig.values.forEach {
                // 分情况创建
                if (it.isFragment) {
                    val destination = fragmentNavigator.createDestination()
                    destination.className = it.clazzName
                    destination.id = it.id
                    destination.addDeepLink(it.pageUrl)
                    navGraph.addDestination(destination)
                } else {
                    val destination = activityNavigator.createDestination()
                    destination.id = it.id
                    destination.addDeepLink(it.pageUrl)
                    destination.setComponentName(
                        ComponentName(
                            AppGlobals.getApplication().packageName,
                            it.clazzName
                        )
                    )

                    navGraph.addDestination(destination)
                }

                //　如果是启动页面
                if (it.asStarter) {
                    navGraph.startDestination = it.id
                }
            }
            controller.graph = navGraph
        }
    }
}