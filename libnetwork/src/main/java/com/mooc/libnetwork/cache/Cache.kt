package com.mooc.libnetwork.cache

import androidx.room.*
import java.io.Serializable

@Entity(tableName = "cache")
data class Cache(
    @PrimaryKey
    val key: String,
    val data: ByteArray?
) : Serializable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Cache

        if (key != other.key) return false
        if (data != null) {
            if (other.data == null) return false
            if (!data.contentEquals(other.data)) return false
        } else if (other.data != null) return false

        return true
    }

    override fun hashCode(): Int {
        var result = key.hashCode()
        result = 31 * result + (data?.contentHashCode() ?: 0)
        return result
    }

}