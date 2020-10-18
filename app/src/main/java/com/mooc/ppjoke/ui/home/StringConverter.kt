package com.mooc.ppjoke.ui.home

class StringConverter {
    companion object {

        // 将过大的数据字面值转为汉字
        @JvmStatic
        fun convertFeedUgc(count: Int): String {
            if (count < 10000) {
                return count.toString()
            }

            return "${count / 10000}万"
        }
    }
}