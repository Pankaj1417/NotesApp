package com.example.mynotes.notes

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import androidx.appcompat.app.AppCompatActivity
import com.example.mynotes.R
import kotlinx.android.synthetic.main.activity_edit_note.*
import kotlinx.android.synthetic.main.activity_notes_deatils.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.content_notes_deatils.*

class NotesDeatils : AppCompatActivity() {

lateinit var content : TextView
lateinit var title : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notes_deatils)
        setSupportActionBar(findViewById(R.id.toolbar))
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        val data  = intent.extras

        //TextViews
        content = notesDetailContent
        title = notesDetailTitle
        content.movementMethod

        //setting data
       title.text = data?.getString("Title")
        content.text = data?.getString("Content")

        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->

            val intent = Intent(this@NotesDeatils, EditNote::class.java)
            intent.putExtra("Title" ,data?.getString("Title"))
            intent.putExtra("Content",data?.getString("Content"))
            intent.putExtra("noteId",data?.getString("noteId"))
            startActivity(intent)

        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}