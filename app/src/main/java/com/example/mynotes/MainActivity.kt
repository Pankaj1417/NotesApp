package com.example.mynotes

import android.content.Intent
import android.os.Bundle
import android.view.*
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.mynotes.auth.LoginActivity
import com.example.mynotes.auth.RegisterActivity
import com.example.mynotes.model.MyViewHolder
import com.example.mynotes.model.Notes
import com.example.mynotes.notes.AddNotes
import com.example.mynotes.notes.EditNote
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
import org.w3c.dom.Document


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

        val query = fStore.collection("notes").document(fAuth.currentUser!!.uid).collection("myNotes").orderBy(
            "title",
            Query.Direction.DESCENDING
        )
        val myNotes = FirestoreRecyclerOptions.Builder<Notes>().setQuery(query, Notes::class.java).setLifecycleOwner(
            this
        ).build()
        val adapter = object : FirestoreRecyclerAdapter<Notes, NoteViewHolder>(myNotes){

            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NoteViewHolder {
                val view = LayoutInflater.from(this@MainActivity).inflate(
                    R.layout.single_note_view,
                    parent,
                    false
                )
                return NoteViewHolder(view)
            }

            override fun onBindViewHolder(holder: NoteViewHolder, p1: Int, note: Notes) {
                val snapshot = snapshots.getSnapshot(p1)
                val id = snapshot.id
                holder.content.text = note.content
                holder.titles.text = note.title
                holder.view.setOnClickListener {
                    val intent : Intent = Intent(holder.view.context, NotesDeatils::class.java)
                    intent.putExtra("Title", note.title)
                    intent.putExtra("Content", note.content)
                    intent.putExtra("noteId",id)
                    holder.view.context.startActivity(intent)
                }
               val menuIcon =  holder.view.menuIcon

                menuIcon.setOnClickListener{
                    val popupMenu: PopupMenu = PopupMenu(menuIcon.context,holder.view)
                    popupMenu.menuInflater.inflate(R.menu.pop_up,popupMenu.menu)
                    popupMenu.setOnMenuItemClickListener(PopupMenu.OnMenuItemClickListener { item ->
                        when(item.itemId) {
                            R.id.action_edit->
                            {
                                val intent = Intent(this@MainActivity, EditNote::class.java)
                                intent.putExtra("Title" ,note.title)
                                intent.putExtra("Content",note.content)
                                intent.putExtra("noteId",id)
                                startActivity(intent)
                            }
                            R.id.action_delete ->
                            {
                                val docRef  = fStore.collection("notes").document(fAuth.currentUser!!.uid).
                                collection("myNotes").document(id).delete().addOnCompleteListener{
                                    if(it.isSuccessful){
                                        Toast.makeText(this@MainActivity,"Noted Deleted",Toast.LENGTH_SHORT).show()
                                    }else{
                                        Toast.makeText(this@MainActivity,"Some Error Occured",Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }
                        }
                        true
                    })
                    popupMenu.show()
                }
            }

        }
        // creating layout manager
        val layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        recyclerView.adapter = adapter
        recyclerView.layoutManager = layoutManager

        //intiializing

        drawerLayout = drawer
        navView = navigation_view
        toolBar = toolbar

        // navView button click managed
        navView.setNavigationItemSelectedListener(this)
        toggle = ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()

    }
//function to check which type of user is currently logged in
    private fun checkUser() : Boolean{
        if(FirebaseAuth.getInstance().currentUser!!.isAnonymous){
            return true
        }
        return false
    }
//function to show alert box for anonymous user
    private fun showAnonymousAlert(){
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Are you sure ?")
        builder.setMessage("You are logged in with Temporary account and may loose current notes")
        builder.setPositiveButton("Sync Notes"){ _, _ ->
            startActivity(Intent(this, RegisterActivity::class.java))
            finish()
        }
        builder.setNegativeButton("Logout"){ _, _ ->
            fAuth.currentUser!!.delete()
            fAuth.signOut()
            startActivity(Intent(this, SplashActivity::class.java))
        }
        builder.setNeutralButton("Cancle"){ _, _ ->
            Toast.makeText(applicationContext, "Signout cancelled", Toast.LENGTH_LONG).show()
            startActivity(intent)
        }
        builder.show()
    }
    //function to show alert box for real user


    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.addBtn -> {
                val intent: Intent = Intent(this, AddNotes::class.java)
                startActivity(intent)
            }

            R.id.logoutBtn -> {
                if (checkUser()) {
                    showAnonymousAlert()
                } else {
                    fAuth.signOut()
                    startActivity(Intent(this,SplashActivity::class.java))
                }
            }
            R.id.syncBtn -> {
                if(checkUser()){
                    startActivity(Intent(applicationContext, RegisterActivity::class.java))
                }else{
                    Toast.makeText(this,"Already synced",Toast.LENGTH_LONG).show()
                }
            }
            R.id.SwitchBtn -> {
                startActivity(Intent(applicationContext, LoginActivity::class.java))
            }
            else->{
                Toast.makeText(this, "Comming Soon", Toast.LENGTH_LONG).show()
            }
        }

        return false
    }


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.option_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == R.id.settings){
            Toast.makeText(this, "Setting selected", Toast.LENGTH_LONG).show()
        }
        return super.onOptionsItemSelected(item)
    }

    fun fabBtnClicked(view: View) {
      startActivity(Intent(applicationContext, AddNotes::class.java))
    }

    private inner class NoteViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val content : TextView = itemView.content
        val titles : TextView = itemView.titles
        val view = itemView
    }


}
