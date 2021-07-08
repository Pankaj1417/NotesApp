package com.example.mynotes.notes

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.example.mynotes.R
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.android.synthetic.main.activity_edit_note.*

class EditNote : AppCompatActivity() {
    lateinit var editContent : EditText
    lateinit var editTitle : EditText
    lateinit var fStore : FirebaseFirestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_note)
        setSupportActionBar(editNoteToolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        //fetching intent data
        val data = intent.extras

        //EditText Views
        editTitle = editNoteTitle
        editContent = editNoteContent
        fStore = FirebaseFirestore.getInstance()

        //setting Data to views
        editTitle.setText(data?.getString("Title"))
        editContent.setText(data?.getString("Content"))

        findViewById<FloatingActionButton>(R.id.editNoteFloatingBtn).setOnClickListener { view ->
            val nTitle : String = editTitle.text.toString()
            val nContent : String = editContent.text.toString()

            if(nTitle.isEmpty() || nContent.isEmpty()){
                Toast.makeText(this,"All fields are compulsory", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            editProgress.visibility = View.VISIBLE
            val docRef : DocumentReference = fStore.collection("notes").document(data?.getString("noteId").toString())
            val note  = HashMap<String,Any>()
            note.put("title",nTitle)
            note.put("content",nContent)
            docRef.update(note).addOnSuccessListener {
                Toast.makeText(this@EditNote,"Note Updated", Toast.LENGTH_LONG).show()
                onBackPressed()
            }.addOnFailureListener{
                editProgress.visibility = View.GONE
                Toast.makeText(this@EditNote,"Some Error Occured ! Try Again", Toast.LENGTH_LONG).show()
            }

        }
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.editNoteToolbar){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}