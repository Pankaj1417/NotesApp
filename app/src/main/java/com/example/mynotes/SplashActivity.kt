package com.example.mynotes

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class SplashActivity : AppCompatActivity() {
    lateinit var fAuth : FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        fAuth = FirebaseAuth.getInstance()
        val r = Runnable {
            if(fAuth.currentUser != null){
                startActivity(Intent(applicationContext,MainActivity::class.java))
                finish()
            }else{
                fAuth.signInAnonymously()
                        .addOnCompleteListener(this) { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(this,"Kindly sign in to ignore any data loss",Toast.LENGTH_LONG).show()
                                startActivity(Intent(applicationContext,MainActivity::class.java))
                                finish()
                            }
                            else {
                                // If sign in fails, display a message to the user.
                                Toast.makeText(this, "Some Error occured try Again !",
                                    Toast.LENGTH_LONG).show()
                            }
                        }

            }

        }
        Handler(Looper.getMainLooper()).postDelayed(r, 2000)
    }
}