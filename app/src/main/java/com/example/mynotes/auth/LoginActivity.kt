package com.example.mynotes.auth

import android.content.ContentValues.TAG
import android.content.Intent
import android.net.wifi.hotspot2.pps.Credential
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import com.example.mynotes.MainActivity
import com.example.mynotes.R
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.nav_header.*

class LoginActivity : AppCompatActivity() {
    lateinit var userPass: EditText
    lateinit var userMail: EditText
    lateinit var fAuth : FirebaseAuth
    lateinit var fStore : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        setSupportActionBar(loginToolbar)
        supportActionBar!!.setDisplayHomeAsUpEnabled(true)
        userMail = loginEmail
        userPass = loginPassword
        fAuth = FirebaseAuth.getInstance()
        fStore= FirebaseFirestore.getInstance()
        0
        // Listener on login btn
        loginBtn.setOnClickListener {
            if(userMail.text.toString().isEmpty() ) {
                userMail.error = "Required"
                return@setOnClickListener
            }
            else if( userPass.text.toString().isEmpty()){
                userPass.error = "Required"
                return@setOnClickListener
            }else{
                val user = fAuth.currentUser
                if(user!!.isAnonymous){
                    showAlert()
                }else{
                    progressBar3.visibility = View.VISIBLE
                    val credential = EmailAuthProvider.getCredential(userMail.text.toString(),userPass.text.toString())
                    fAuth.signInWithCredential(credential).addOnCompleteListener{
                        if(it.isSuccessful){
                            progressBar3.visibility = View.GONE
                            Toast.makeText(this,"Sign in successful",Toast.LENGTH_LONG).show()
                            startActivity(Intent(this,MainActivity::class.java))
                        }else{
                            Toast.makeText(this,"Failed",Toast.LENGTH_LONG).show()
                        }
                    }
                }

            }

        }

        // listener on create account textview
        createAccountTv.setOnClickListener {
           startActivity(Intent(this,RegisterActivity::class.java))
            finish()
        }

    }

    private fun showAlert(){
        val alert = AlertDialog.Builder(this)
        alert.setTitle("Notes in Danger")
        alert.setMessage("To save current notes please create a new account first")
        alert.setNeutralButton("Cancle"){
            _,_ ->
            startActivity(Intent(this,MainActivity::class.java))
            finish()
        }
        alert.setPositiveButton("Create"){
            _,_ ->
            startActivity(Intent(this,RegisterActivity::class.java))
            finish()
        }
        alert.setNegativeButton("Continue"){
            _,_ ->
            progressBar3.visibility = View.VISIBLE
            val credential = EmailAuthProvider.getCredential(userMail.text.toString(),userPass.text.toString())
            fAuth.signInWithCredential(credential).addOnCompleteListener{
                if(it.isSuccessful){
                    progressBar3.visibility = View.GONE
                    Toast.makeText(this,"Sign in successful",Toast.LENGTH_LONG).show()
                    startActivity(Intent(this,MainActivity::class.java))
                    finish()
                }else{
                    Toast.makeText(this,Log.ERROR,Toast.LENGTH_LONG).show()
                }
            }
        }
        alert.show()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}