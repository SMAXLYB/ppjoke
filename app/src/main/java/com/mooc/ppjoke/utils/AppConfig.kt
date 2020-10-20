package com.mooc.ppjoke.utils

import com.alibaba.fastjson.JSON
import com.alibaba.fastjson.TypeReference
import com.mooc.libcommon.AppGlobals
import com.mooc.ppjoke.model.BottomBar
import com.mooc.ppjoke.model.Destination
import com.mooc.ppjoke.model.SofaTab
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.util.*


class AppConfig {
    companion object {

        private var sDestConfig: HashMap<String, Destination>? = null
        private var sBottomBar: BottomBar? = null
        private var sSofaTab: SofaTab? = null
        private var sFindTabConfig: SofaTab? = null

        @JvmStatic
        fun getSofaTabConfig(): SofaTab {
            if (sSofaTab == null) {
                val content = parseFile("sofa_tabs_config.json")
                sSofaTab = JSON.parseObject(content, SofaTab::class.java)
                sSofaTab!!.tabs.sortWith(Comparator { o1, o2 -> if (o1.index < o2.index) -1 else 1 })
            }
            return sSofaTab as SofaTab
        }

        @JvmStatic
        fun getDestConfig(): HashMap<String, Destination> {
            if (sDestConfig == null) {
                val content = parseFile("destination.json")
                sDestConfig = JSON.parseObject(
                    content,
                    object : TypeReference<HashMap<String, Destination>>() {}.type
                )
            }
            return sDestConfig as HashMap<String, Destination>
        }

        @JvmStatic
        fun getBottomBar(): BottomBar {
            if (sBottomBar == null) {
                val content = parseFile("main_tabs_config.json")
                sBottomBar = JSON.parseObject(content, BottomBar::class.java)
            }
            return sBottomBar as BottomBar
        }

        @JvmStatic
        private fun parseFile(fileName: String): String {
            val assets = AppGlobals.getApplication().assets
            var inputStream: InputStream? = null
            var reader: BufferedReader? = null
            val builder = StringBuilder()

            try {
                inputStream = assets.open(fileName)
                reader = BufferedReader(InputStreamReader(inputStream))
                var line: String? = reader.readLine()
                while (line != null) {
                    builder.append(line)
                    line = reader.readLine()
                }
            } catch (e: IOException) {
                e.printStackTrace()
            } finally {
                if (inputStream != null) {
                    try {
                        inputStream.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
                if (reader != null) {
                    try {
                        reader.close()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            return builder.toString()
        }
    }
}