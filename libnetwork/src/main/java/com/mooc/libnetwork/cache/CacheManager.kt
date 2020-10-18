package com.mooc.libnetwork.cache

import android.util.Log
import java.io.ByteArrayInputStream
import java.io.ByteArrayOutputStream
import java.io.ObjectInputStream
import java.io.ObjectOutputStream

class CacheManager {
    companion object {
        fun <T> save(key: String, body: T) {
            val data = toByteArray(body)
            val cache = Cache(key, data)
            CacheDatabase.instance.getDao().save(cache)
        }

        fun <T> getCache(key: String): T? {
            val cache = CacheDatabase.instance.getDao().getCache(key)
            if (cache?.data != null) {
                return toObject<T>(cache.data)
            }
            return null
        }

        private fun <T> toObject(data: ByteArray): T {
            return ByteArrayInputStream(data).use { bis ->
                ObjectInputStream(bis).use {
                    it.readObject() as T
                }
            }
        }

        private fun <T> toByteArray(body: T): ByteArray {
            return ByteArrayOutputStream().use { bos ->
                ObjectOutputStream(bos).use {
                    it.writeObject(body)
                    it.flush()
                    bos.toByteArray()
                }
            }
        }
    }
}
