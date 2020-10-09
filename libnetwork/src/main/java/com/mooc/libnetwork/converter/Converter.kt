package com.mooc.libnetwork.converter

import java.lang.reflect.Type

interface Converter<T> {
    fun convert(response: String, type: Type): T?

    // fun convert(response: String, clazz: Class<*>): T?
}