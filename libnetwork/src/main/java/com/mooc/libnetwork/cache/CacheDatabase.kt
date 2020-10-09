package com.mooc.libnetwork.cache

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.mooc.libcommon.AppGlobals

// 抽象类,运行时会自动生成实现类
// @Database(entities = [Cache::class], version = 1, exportSchema = true)
// abstract class CacheDatabase : RoomDatabase() {
//     companion object {
//         val instance = CacheDatabaseHolder.holder
//     }
//
//     private object CacheDatabaseHolder {
//         val holder = Room.databaseBuilder<CacheDatabase>(
//             AppGlobals.getApplication(),
//             CacheDatabase::class.java,
//             "ppjoke_cache"
//         )
//             // .allowMainThreadQueries()
//             .build()
//     }
//
//     abstract fun getDao(): CacheDao
//
// }