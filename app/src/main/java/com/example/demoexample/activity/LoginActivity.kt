package com.example.demoexample.activity

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.example.demoexample.R
import com.example.demoexample.utils.AnimationTranslate
import com.example.demoexample.utils.Glob
import com.example.demoexample.utils.ProgressDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_login.ed_email
import kotlinx.android.synthetic.main.activity_login.ed_password
import kotlinx.android.synthetic.main.activity_login.txt_signup
import kotlinx.android.synthetic.main.activity_signup.*

class LoginActivity : AppCompatActivity(), View.OnClickListener {
    private var progressDialog: ProgressDialog? = null
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        auth = FirebaseAuth.getInstance()
        Init()
    }

    private fun Init() {
        txt_login.setOnClickListener(this)
        txt_signup.setOnClickListener(this)
        progressDialog = ProgressDialog(this)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setCanceledOnTouchOutside(false)
    }

    override fun onClick(v: View?) {
        when (v) {
            txt_login -> {
                if (validation()) {
                    login()
                }
            }
            txt_signup -> {
                val intent = Intent(applicationContext, SignupActivity::class.java)
                startActivity(intent)
                AnimationTranslate().nextAnimation(this)
            }
        }
    }

    private fun login() {
        progressDialog!!.show()
        auth!!.signInWithEmailAndPassword(ed_email.text.toString(),ed_password.text.toString()).addOnCompleteListener(this, OnCompleteListener
        { task ->
            progressDialog!!.dismiss()
            if(task.isSuccessful) {
                val sh = getSharedPreferences(Glob().Sh_prefName, Context.MODE_PRIVATE)
                val editor = sh.edit()
                editor.putBoolean(Glob().isUserLogin,true).apply()
                val intent = Intent(this, DashboardActivity::class.java)
                startActivity(intent)
                finish()
                AnimationTranslate().nextAnimation(this)
            }else {
                Glob().displaymessage(this,getString(R.string.loginfailed))
            }
        })
    }

    private fun validation(): Boolean {
        if (ed_email.text.isNullOrEmpty()) {
            ed_email.requestFocus()
            ed_email.setError(resources.getString(R.string.erroremail))
            return false
        } else if (!Glob().isValidEmail(ed_email.text.toString())) {
            ed_email.requestFocus()
            ed_email.setError(getString(R.string.validemail))
            return false
        } else if (ed_password.text.isNullOrEmpty()) {
            ed_password.requestFocus()
            ed_password.setError(resources.getString(R.string.errorpassword))
            return false
        } else {
            return true
        }
    }
}