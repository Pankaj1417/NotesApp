package com.example.mynotes

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import android.widget.TextView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import androidx.appcompat.app.AppCompatActivity
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
        content = notesDetailContent
        title = notesDetailTitle
        content.movementMethod
        notesDetailContent.text = data?.getString("Content")
        notesDetailTitle.text = data?.getString("Title")
        findViewById<FloatingActionButton>(R.id.fab).setOnClickListener { view ->
            Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                    .setAction("Action", null).show()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home){
            onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }
}