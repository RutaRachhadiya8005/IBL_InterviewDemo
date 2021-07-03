package com.example.demoexample.activity

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.demoexample.R
import com.example.demoexample.modal.User
import com.example.demoexample.utils.AnimationTranslate
import com.example.demoexample.utils.Glob
import com.example.demoexample.utils.ProgressDialog
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_signup.*


class SignupActivity : AppCompatActivity(), View.OnClickListener {
    private var firebaseDatabase: FirebaseDatabase? = null
    private var progressDialog: ProgressDialog? = null
    private var auth: FirebaseAuth? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        auth = FirebaseAuth.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
        Init()
    }

    private fun Init() {
        txt_signup.setOnClickListener(this)
        txt_backtologin.setOnClickListener(this)
        progressDialog = ProgressDialog(this)
        progressDialog!!.setCancelable(false)
        progressDialog!!.setCanceledOnTouchOutside(false)
    }

    override fun onClick(v: View?) {
        when (v) {
            txt_signup -> {
                if (validation()) {
                    signup()
                }
            }
            txt_backtologin -> {
                finish()
                AnimationTranslate().previewAnimation(this)
            }
        }

    }

    private fun validation(): Boolean {
        if (ed_name.text.isNullOrEmpty()) {
            ed_name.requestFocus()
            ed_name.setError(resources.getString(R.string.errorname))
            return false
        } else if (ed_email.text.isNullOrEmpty()) {
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

    override fun onBackPressed() {
        super.onBackPressed()
        AnimationTranslate().previewAnimation(this)
    }

    private fun signup() {
        progressDialog!!.show()
        auth!!.createUserWithEmailAndPassword(
            ed_email.text.toString(),
            ed_password.text.toString()
        ).addOnCompleteListener(
            this, OnCompleteListener
            { task ->
                if (task.isSuccessful) {
                    addnewUserdata()
                } else {
                    progressDialog!!.dismiss()
                    Glob().displaymessage(this, getString(R.string.registerfail))
                }
            })
    }

    private fun addnewUserdata() {
        val user = User()
        user.name = ed_name.text.toString()
        user.email = ed_email.text.toString()
        val db = FirebaseFirestore.getInstance()
        val dbCourses = db.collection("User")
        dbCourses.add(user).addOnSuccessListener {
            progressDialog!!.dismiss()
            val sh = getSharedPreferences(Glob().Sh_prefName, Context.MODE_PRIVATE)
            val editor = sh.edit()
            editor.putBoolean(Glob().isUserLogin,true).apply()
            val intent = Intent(this, DashboardActivity::class.java)
            startActivity(intent)
            finish()
            AnimationTranslate().nextAnimation(this)
        }
            .addOnFailureListener { e ->
                progressDialog!!.dismiss()
                Glob().displaymessage(this, getString(R.string.registerfail))
            }
    }
}