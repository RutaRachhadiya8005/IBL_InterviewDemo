package com.example.demoexample.utils

import android.app.Activity
import android.widget.Toast
import com.example.demoexample.activity.SignupActivity
import java.util.regex.Pattern

class Glob {
    val Sh_prefName: String = "DemoExample"
    val isUserLogin: String = "isLogin"

    fun isValidEmail(email: String?): Boolean {
        val EMAIL_STRING =
            "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@" + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$"
        val p = Pattern.compile(EMAIL_STRING)
        val m = p.matcher(email)
        return m.matches()
    }

    fun displaymessage(activity: Activity, message: String) {
        Toast.makeText(activity, message, Toast.LENGTH_LONG).show()
    }


    interface SelectedGraph {
        fun deletegraph(pos: Int)
        fun editgraph(pos: Int)
    }

}