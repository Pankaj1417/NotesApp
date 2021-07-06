package com.example.mynotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mynotes.model.MyAdapter
import com.example.mynotes.model.Notes
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.single_note_view.*
import kotlinx.android.synthetic.main.single_note_view.view.*
import java.util.*
import kotlin.collections.ArrayList

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    //Variables
    private lateinit var drawerLayout : DrawerLayout
    lateinit var toggle : ActionBarDrawerToggle
    lateinit var navView : NavigationView
    lateinit var toolBar : androidx.appcompat.widget.Toolbar
    lateinit var fStore : FirebaseFirestore
    private var adapter: NotesRecyclerAdapter? = null

    //onCreate fun
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // toolbar support
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        fStore = FirebaseFirestore.getInstance()
        presentNotes()
        // creating layout manager
        val layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        //intiializing

        drawerLayout = drawer
        navView = navigation_view
        toolBar = toolbar

        // navView button click managed
        navView.setNavigationItemSelectedListener(this)
        toggle = ActionBarDrawerToggle(this,drawerLayout,toolbar,R.string.open,R.string.close)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()

    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.addBtn -> {
                val intent : Intent = Intent(this,AddNotes::class.java)
                startActivity(intent)
            }
            else->{
                Toast.makeText(this,"ButtonClicked",Toast.LENGTH_LONG).show()
            }
        }

        return false
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.settings){
            Toast.makeText(this,"Setting selected",Toast.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }

    //clicks Managed
    fun noteItemClicked(titles: String, content: String) {
        Toast.makeText(this, "Item Clicked" , Toast.LENGTH_SHORT).show()
    }

    fun fabBtnClicked(view : View){
        startActivity(Intent(this,AddNotes::class.java))
    }

    private fun presentNotes(){
        val query = fStore.collection("products").orderBy("title", Query.Direction.ASCENDING)
        val myNotes = FirestoreRecyclerOptions.Builder<Notes>().setQuery(query, Notes::class.java).build()
        adapter = NotesRecyclerAdapter(myNotes)

    }
    private inner class NoteViewHolder internal constructor(val view: View) : RecyclerView.ViewHolder(view) {
        val content : TextView = view.content
        val titles : TextView = view.titles
    }
    private inner class NotesRecyclerAdapter(allNotes: FirestoreRecyclerOptions<Notes>) : FirestoreRecyclerAdapter<Notes, NoteViewHolder>(allNotes) {

        override fun onBindViewHolder(noteViewHolder: NoteViewHolder, position: Int, notes: Notes) {
            val currentTitle = notes.title?.get(position)
            val currentContent = notes.content?.get(position)
            noteViewHolder.content.text = currentContent.toString()
            noteViewHolder.titles.text = currentTitle.toString()
            noteViewHolder.view.setOnClickListener {
                val intent : Intent = Intent(noteViewHolder.view.context,NotesDeatils::class.java)
                intent.putExtra("Title" , currentTitle)
                intent.putExtra("Content",currentContent)
                noteViewHolder.view.context.startActivity(intent)
            }
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.single_note_view, parent, false)
            return NoteViewHolder(view)
        }
    }

    override fun onStart() {
        super.onStart()
        adapter!!.startListening()
    }

    override fun onStop() {
        super.onStop()
        if(adapter!= null){
            adapter!!.stopListening()
        }

    }
}
