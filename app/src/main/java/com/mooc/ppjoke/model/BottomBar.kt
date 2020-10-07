package com.mooc.ppjoke.model

class BottomBar {
    var activeColor: String = ""
    var inActiveColor: String = ""
    var selectTab: Int = 0
    var tabs: List<Tab> = listOf<Tab>()

    class Tab {
        var enable: Boolean = false
        var index: Int = 0
        var pageUrl: String = ""
        var size: Int = 0
        var tintColor: String = ""
        var title: String = ""
    }
}