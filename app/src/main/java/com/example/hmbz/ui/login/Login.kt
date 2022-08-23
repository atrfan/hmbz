package com.example.hmbz.ui.login

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.hmbz.HuamaoApplication
import com.example.hmbz.databinding.ActivityLoginBinding
import com.example.hmbz.logic.network.HuamaoNetwork
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class Login : AppCompatActivity() {
    lateinit var loginBinding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        loginBinding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(loginBinding.root)
        loginBinding.loginIn.setOnClickListener{
            val account = loginBinding.userName.text.toString()
            val password = loginBinding.passWord.text.toString()
            var code = "000"
            runBlocking {
                launch(Dispatchers.IO) {
                    code = HuamaoNetwork.userLogin(account,password)
                }.join()
            }
            when (code) {
                "10000" -> Toast.makeText(HuamaoApplication.context,"登录成功",Toast.LENGTH_SHORT).show()
                "40002" -> Toast.makeText(HuamaoApplication.context,"账号不存在，请确认后重试",Toast.LENGTH_SHORT).show()
                "40004" -> Toast.makeText(HuamaoApplication.context,"账号或密码错误，请确认后重试",Toast.LENGTH_SHORT).show()
                else -> {
                    Log.w("Login","出现了新的code，请注意！！")
                }
            }
        }
    }
}