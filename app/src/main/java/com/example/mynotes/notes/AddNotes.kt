package com.example.mynotes.notes

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import com.example.mynotes.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_add_notes.*
import kotlinx.android.synthetic.main.content_add_notes.*
import java.util.*
import kotlin.collections.HashMap

class AddNotes : AppCompatActivity() {
lateinit var fStore : FirebaseFirestore
lateinit var noteContent : EditText
lateinit var noteTitle : EditText
lateinit var saveProgress : ProgressBar
lateinit var fUser : FirebaseUser
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_notes)
        setSupportActionBar(findViewById(R.id.toolbar))
        fStore = FirebaseFirestore.getInstance()
        noteTitle = addNoteTitle
        noteContent = addNoteContent
        saveProgress = addNoteProgressBar
        fUser = FirebaseAuth.getInstance().currentUser!!

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->

            val nTitle : String = noteTitle.text.toString()
            val nContent : String = noteContent.text.toString()

            if(nTitle.isEmpty() || nContent.isEmpty()){
                Toast.makeText(this,"All fields are compulsory", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            saveProgress.visibility = View.VISIBLE
            val docRef : DocumentReference = fStore.collection("notes").document(fUser.uid).collection("myNotes").document()
            val note  = HashMap<String,String>()
            note.put("title",nTitle)
            note.put("content",nContent)
            docRef.set(note).addOnSuccessListener {
                Toast.makeText(this@AddNotes,"Note Added",Toast.LENGTH_LONG).show()
                onBackPressed()
            }.addOnFailureListener{
                saveProgress.visibility = View.GONE
                Toast.makeText(this@AddNotes,"Some Error Occured ! Try Again",Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.close_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.close){
            Toast.makeText(this,"Not Saved",Toast.LENGTH_LONG).show()
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}