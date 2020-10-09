package com.mooc.libnetwork.cache

import androidx.room.*

// @Dao
// interface CacheDao {
//     @Insert(onConflict = OnConflictStrategy.REPLACE)
//     fun save(cache: Cache)
//
//     @Query("SELECT * FROM cache WHERE `key` = :key")
//     fun getCache(key: String) : Cache
//
//     @Delete
//     fun delete(cache: Cache)
//
//     @Update(onConflict = OnConflictStrategy.REPLACE)
//     fun update(cache: Cache)
//
// }