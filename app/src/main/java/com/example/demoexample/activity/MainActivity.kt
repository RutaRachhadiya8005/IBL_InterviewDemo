package com.example.demoexample.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import com.example.demoexample.R
import com.example.demoexample.utils.AnimationTranslate
import com.example.demoexample.utils.Glob

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        Init()
    }

    private fun Init() {
        Handler().postDelayed({
            val sh = getSharedPreferences(Glob().Sh_prefName, Context.MODE_PRIVATE)
            val isUserLogin = sh.getBoolean(Glob().isUserLogin, false)
            if (!isUserLogin) {
                intent = Intent(applicationContext, LoginActivity::class.java)
            } else {
                intent = Intent(applicationContext, DashboardActivity::class.java)
            }
            startActivity(intent)
            finish()
            AnimationTranslate().nextAnimation(this)
        }, 1000)
    }
}