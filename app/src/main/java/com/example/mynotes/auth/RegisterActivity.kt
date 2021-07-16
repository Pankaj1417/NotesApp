package com.example.mynotes.auth

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import com.example.mynotes.MainActivity
import com.example.mynotes.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_register.*

class RegisterActivity : AppCompatActivity() {
    lateinit var fAuth : FirebaseAuth
    lateinit var name : String
    lateinit var mail : String
    lateinit var pass : String
    lateinit var cnfPass : String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        setSupportActionBar(regToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)

        //initializing
        fAuth = FirebaseAuth.getInstance()

        createAccountBtn.setOnClickListener {
            name = userName.text.toString()
            mail = userEmail.text.toString()
            pass = userPassword.text.toString()
            cnfPass = passwordConfirm.text.toString()

            if(name.isEmpty() || pass.isEmpty() || mail.isEmpty()|| cnfPass.isEmpty()){
                Toast.makeText(applicationContext , "all Fields Required",Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            else if(pass != cnfPass){
                passwordConfirm.error = "Confirm Password do not match"
                return@setOnClickListener
            }else{
                progressBar4.visibility = View.VISIBLE
                val credential = EmailAuthProvider.getCredential(mail,pass)
                fAuth.currentUser!!.linkWithCredential(credential)
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(applicationContext,"Notes Synced" , Toast.LENGTH_LONG).show()
                                startActivity(Intent(this,MainActivity::class.java))
                                finish()
                            } else {
                                progressBar4.visibility = View.GONE
                                Toast.makeText(applicationContext, Log.ERROR , Toast.LENGTH_LONG).show()
                            }
                        }
            }
        }
        loginTv.setOnClickListener {
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        startActivity(Intent(this,MainActivity::class.java))
        finish()
        return super.onOptionsItemSelected(item)
    }
}