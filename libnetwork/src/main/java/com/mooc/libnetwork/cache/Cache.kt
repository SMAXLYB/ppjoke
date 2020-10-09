package com.mooc.libnetwork.cache

import androidx.room.*
import java.io.Serializable

// @Entity(tableName = "cache")
// data class Cache(
//     @PrimaryKey
//     val key: String,
//     val data: ByteArray
// ) : Serializable {
//     override fun equals(other: Any?): Boolean {
//         if (this === other) return true
//         if (javaClass != other?.javaClass) return false
//
//         other as Cache
//
//         if (key != other.key) return false
//         if (!data.contentEquals(other.data)) return false
//
//         return true
//     }
//
//     override fun hashCode(): Int {
//         var result = key.hashCode()
//         result = 31 * result + data.contentHashCode()
//         return result
//     }
// }