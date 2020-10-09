package com.mooc.libnetwork.converter

import com.alibaba.fastjson.JSON
import java.lang.reflect.Type

class JsonConverter : Converter<Any> {
    override fun convert(response: String, type: Type): Any? {
        val jsonObject = JSON.parseObject(response)
        val data = jsonObject.getJSONObject("data")
        data?.let {
            val dataResult = data["data"]
            dataResult?.let {
                return JSON.parseObject<Any>(it.toString(), type)
            }
        }
        return null
    }

    // 暂时不用
    // override fun convert(response: String, clazz: Class<*>): Any? {
    //     return null
    // }
}