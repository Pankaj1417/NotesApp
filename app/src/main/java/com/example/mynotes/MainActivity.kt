package com.example.mynotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.*
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mynotes.auth.LoginActivity
import com.example.mynotes.model.Notes
import com.example.mynotes.notes.AddNotes
import com.example.mynotes.notes.NotesDeatils
import com.firebase.ui.firestore.FirestoreRecyclerAdapter
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.single_note_view.view.*


class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    //Variables
    private lateinit var drawerLayout : DrawerLayout
    lateinit var toggle : ActionBarDrawerToggle
    lateinit var navView : NavigationView
    lateinit var toolBar : androidx.appcompat.widget.Toolbar
    lateinit var fStore : FirebaseFirestore
    lateinit  var fAuth : FirebaseAuth
    //onCreate fun
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // toolbar support
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        fStore = FirebaseFirestore.getInstance()
        fAuth = FirebaseAuth.getInstance()

        val query = fStore.collection("notes").orderBy("title",Query.Direction.DESCENDING)
        val myNotes = FirestoreRecyclerOptions.Builder<Notes>().setQuery(query, Notes::class.java).setLifecycleOwner(this).build()
        val adapter = object : FirestoreRecyclerAdapter<Notes, NoteViewHolder>(myNotes){

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
                val view = LayoutInflater.from(this@MainActivity).inflate(R.layout.single_note_view,parent,false)
                return NoteViewHolder(view)
            }

            override fun onBindViewHolder(holder: NoteViewHolder, p1: Int, note: Notes) {
                val id = fStore.collection("notes").id
                holder.content.text = note.content
                holder.titles.text = note.title
                holder.view.setOnClickListener {
                    val intent : Intent = Intent(holder.view.context, NotesDeatils::class.java)
                    intent.putExtra("Title" , note.title)
                    intent.putExtra("Content",note.content)
                    intent.putExtra("noteId",id)
//                    intent.putExtra("noteId",)
                    holder.view.context.startActivity(intent)
                }
            }

        }
        // creating layout manager
        val layoutManager = StaggeredGridLayoutManager(2,StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

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

    private fun checkUser() : Boolean{
        if(FirebaseAuth.getInstance().currentUser!!.isAnonymous){
            return true
        }
        return false
    }

    private fun showAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure ?")
        builder.setMessage("You are not logged in if you signout you will loose all your data")
        builder.setPositiveButton("Sync Now"){
                _, which ->
            startActivity(Intent(this,LoginActivity::class.java))
            finish()
        }
        builder.setNegativeButton("continue"){
                _, which ->
            fAuth.signOut()
            startActivity(Intent(this,SplashActivity::class.java))
        }
        builder.setNeutralButton("cancle"){
                _, which ->
            Toast.makeText(applicationContext,"Signout cancelled",Toast.LENGTH_LONG).show()
        }
        builder.show()
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.addBtn -> {
                val intent : Intent = Intent(this, AddNotes::class.java)
                startActivity(intent)
            }
            R.id.logoutBtn -> {
                    if(checkUser()){
                        showAlert()
                    }else{
                       fAuth.signOut()
                    }
            }

            R.id.syncBtn -> {
                startActivity(Intent(applicationContext,LoginActivity::class.java))
                finish()
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

    fun fabBtnClicked() {
        startActivity(Intent(this, AddNotes::class.java))
    }

    private inner class NoteViewHolder internal constructor(val view: View) : RecyclerView.ViewHolder(view) {
        val content : TextView = view.content
        val titles : TextView = view.titles

    }


}
