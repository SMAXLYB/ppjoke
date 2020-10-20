package com.mooc.ppjoke.ui.login

import android.content.Context
import android.content.Intent
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.mooc.libnetwork.cache.CacheManager
import com.mooc.ppjoke.model.User

object UserManager {
    private val userLiveData = MutableLiveData<User>()
    private var mUser: User? = null

    private const val KEY_CACHE_USER = "cache_user"

    init {
        val cache = CacheManager.getCache<User>(KEY_CACHE_USER)

        // 如果时间未过期,表示已经登陆
        if (cache != null && cache.expires_time > System.currentTimeMillis()) {
            mUser = cache
        }
    }

    fun save(user: User) {
        mUser = user
        CacheManager.save(KEY_CACHE_USER, user)
        if (userLiveData.hasObservers()) {
            userLiveData.postValue(user)
        }
    }

    fun login(context: Context): LiveData<User> {
        val intent = Intent(context, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
        return userLiveData
    }

    // 是否已经登陆
    fun isLogin(): Boolean {
        return if (mUser == null)
            false
        else
            mUser!!.expires_time > System.currentTimeMillis()
    }

    fun getUser(): User? {
        return if (isLogin())
            mUser
        else
            null
    }

    fun getUserId(): Long {
        return if (isLogin())
            mUser!!.userId
        else
            0

    }

    // fun refresh(): LiveData<User?> {
    //     if (!isLogin) {
    //         return login(AppGlobals.getApplication())
    //     }
    //     val liveData = MutableLiveData<User?>()
    //     get<Any>("/user/query")
    //         .addParam("userId", userId)
    //         .execute(object : JsonCallback<User?>() {
    //             override fun onSuccess(response: ApiResponse<User>) {
    //                 save(response.body)
    //                 liveData.postValue(user)
    //             }
    //
    //             override fun onError(response: ApiResponse<User>) {
    //                 ArchTaskExecutor.getMainThreadExecutor().execute {
    //                     Toast.makeText(
    //                         AppGlobals.getApplication(),
    //                         response.message,
    //                         Toast.LENGTH_SHORT
    //                     ).show()
    //                 }
    //                 liveData.postValue(null)
    //             }
    //         })
    //     return liveData
    // }
    //
    // fun logout() {
    //     CacheManager.delete(KEY_CACHE_USER, mUser)
    //     mUser = null
    // }
}