package com.mooc.ppjoke.ui.login

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.mooc.libnetwork.ApiResponse
import com.mooc.libnetwork.ApiService
import com.mooc.libnetwork.JsonCallback
import com.mooc.ppjoke.R
import com.mooc.ppjoke.model.User
import com.tencent.connect.UserInfo
import com.tencent.connect.auth.QQToken
import com.tencent.connect.common.Constants
import com.tencent.tauth.IUiListener
import com.tencent.tauth.Tencent
import com.tencent.tauth.UiError
import org.json.JSONException
import org.json.JSONObject

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var actionClose: View
    private lateinit var actionLogin: View

    private var tencent: Tencent? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        actionClose = findViewById(R.id.action_close)
        actionLogin = findViewById(R.id.action_login)

        actionClose.setOnClickListener(this)
        actionLogin.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        if (v.id == R.id.action_close) {
            finish()
        } else if (v.id == R.id.action_login) {
            login()
        }
    }

    private fun login() {
        if (tencent == null) {
            tencent = Tencent.createInstance("101911858", applicationContext)
        }
        tencent?.login(this, "all", loginListener)
    }

    private var loginListener = object : IUiListener {
        override fun onComplete(o: Any) {
            val response = o as JSONObject
            try {
                val openid = response.getString("openid")
                val access_token = response.getString("access_token")
                val expires_in = response.getString("expires_in")
                val expires_time = response.getLong("expires_time")
                tencent!!.openId = openid
                tencent!!.setAccessToken(access_token, expires_in)
                val qqToken = tencent!!.qqToken
                getUserInfo(qqToken, expires_time, openid)
            } catch (e: JSONException) {
                e.printStackTrace()
            }
        }

        override fun onError(uiError: UiError) {
            Toast.makeText(applicationContext, "登录失败:reason$uiError", Toast.LENGTH_SHORT).show()
        }

        override fun onCancel() {
            Toast.makeText(applicationContext, "登录取消", Toast.LENGTH_SHORT).show()
        }
    }

    private fun getUserInfo(qqToken: QQToken, expires_time: Long, openid: String) {
        val userInfo = UserInfo(applicationContext, qqToken)
        userInfo.getUserInfo(object : IUiListener {
            override fun onComplete(o: Any) {
                val response = o as JSONObject
                try {
                    val nickname = response.getString("nickname")
                    val figureurl_2 = response.getString("figureurl_2")
                    save(nickname, figureurl_2, openid, expires_time)
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }

            override fun onError(uiError: UiError) {
                Toast.makeText(applicationContext, "登录失败:reason$uiError", Toast.LENGTH_SHORT).show()
            }

            override fun onCancel() {
                Toast.makeText(applicationContext, "登录取消", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun save(nickname: String, avatar: String, openid: String, expires_time: Long) {
        ApiService.get<User>("/user/insert")
            .addParam("name", nickname)
            .addParam("avatar", avatar)
            .addParam("qqOpenId", openid)
            .addParam("expires_time", expires_time)
            .execute(object : JsonCallback<User>() {

                override fun onSuccess(response: ApiResponse<User>) {
                    if (response.body != null) {
                        UserManager.save(response.body as User)
                        finish()
                    } else {
                        runOnUiThread {
                            Toast.makeText(
                                applicationContext,
                                "登陆失败",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                    }
                }

                override fun onError(response: ApiResponse<User>) {
                    runOnUiThread {
                        Toast.makeText(
                            applicationContext,
                            "登陆失败,msg:" + response.message,
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            })
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, loginListener)
        }
    }
}