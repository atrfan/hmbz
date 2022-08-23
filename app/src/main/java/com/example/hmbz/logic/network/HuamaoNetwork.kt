package com.example.hmbz.logic.network

import okhttp3.FormBody
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.internal.userAgent
import org.json.JSONObject

object HuamaoNetwork {
    private const val USERAGENT =
        "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/104.0.0.0 Safari/537.36"
    private val client = OkHttpClient()

    private const val REFERER = "https://www.colorpuss.com/login/"

    private const val defaultCookie =
        "PHPSESSID=4pbd06v6ibfrle50ernvl2ok10; session_id=4pbd06v6ibfrle50ernvl2ok10; Hm_lvt_c30a5e2c0bff3f0917e4091ba243e59a=1661222295; Hm_lpvt_c30a5e2c0bff3f0917e4091ba243e59a=1661222295; __gads=ID=aac220bca68b4b77-225cf7f0c1d50030:T=1661222295:RT=1661222295:S=ALNI_MbtnirZA9Y14wRbskL0YrMD23jIUw; __gpi=UID=000008eb40faebb5:T=1661222295:RT=1661222295:S=ALNI_MY09MltGA13HieZxIjQOsqwCTDYeQ"

    private var cookie = ""

    suspend fun loadData(url: String): Response {
        val resCookie = if (cookie == "") defaultCookie else cookie
        val request = Request.Builder()
            .url(url)
            .addHeader("user-agnet", userAgent)
            .addHeader("cookie",resCookie)
            .build()

        return client.newCall(request).execute()
    }


    /**
     *
     * 登录并获取cookie，每个账号的cookie都是唯一的
     * 放回一个code，用于表示请求登录的结果
     *  code == 10000 ,表示 登录成功
     *  code == 40002 ,表示 账号不存在
     *  code == 40004 ，表示 密码错误
     */

    suspend fun userLogin(userName: String, passWord: String): String {
        pretreatment(userName, passWord)
        val form = FormBody.Builder()
            .add("account", userName)
            .add("password", passWord)
            .add("survival_time", "0")
            .add("verify", "")
            .build()
        val request = Request.Builder()
            .url(" https://api.colorpuss.com/user/auth/login/")
            .post(form)
            .addHeader("User-agent", USERAGENT)
            .addHeader("Referer", REFERER)
            .addHeader("Cookie", cookie)
            .build()
        val response = client.newCall(request).execute()
        val list: List<String> = response.headers.values("Set-Cookie")
        for (str in list) {
            cookie = cookie.plus(str.split(";")[0]).plus(";")
        }
        val json = JSONObject(response.body?.string().toString())
        val code = json.get("code")
        response.close()
        return code.toString()
    }


    suspend fun pretreatment(userName: String, passWord: String) {
        val url1 = "https://www.colorpuss.com/loginpass/"
        val form = FormBody.Builder()
            .add("only", userName)
            .add("password", passWord)
            .add("remembe", "false")
            .build()
        val req1: Request = Request.Builder()
            .url(url1)
            .post(form)
            .addHeader(
                "User-Agent",
                "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/93.0.4577.63 Safari/537.36"
            )
            .addHeader("Referer", "https://www.colorpuss.com/login/")
            .build()
        val resp = client.newCall(req1).execute()
        val list = resp.headers.values("Set-Cookie")
        for (value in list) {
            val s = value.split(";")[0]
            cookie = "$cookie$s;"
        }
        resp.close()
    }
}