package com.example.mynotes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.mynotes.model.MyAdapter
import com.google.android.material.navigation.NavigationView
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.content_main.*
import kotlinx.android.synthetic.main.single_note_view.*
import java.util.*

class MainActivity : AppCompatActivity(),NavigationView.OnNavigationItemSelectedListener {
    //Variables
    private lateinit var drawerLayout : DrawerLayout
    lateinit var toggle : ActionBarDrawerToggle
    lateinit var navView : NavigationView
    lateinit var toolBar : androidx.appcompat.widget.Toolbar
    lateinit var adapter: MyAdapter
    lateinit var titles : ArrayList<String>
    lateinit var contents : ArrayList<String>

    //onCreate fun
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // toolbar support
        setSupportActionBar(toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        // creating layout manager
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager

        //intiializing
        drawerLayout = drawer
        navView = navigation_view
        toolBar = toolbar
        titles = ArrayList<String>()
        contents = ArrayList<String>()
        update()
        adapter = MyAdapter(titles,contents)
        recyclerView.adapter = adapter

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
    private fun update(){
        titles.add("Hey")
        contents.add("hello")
        titles.add("Hey")
        contents.add("hello")
        titles.add("Hey")
        contents.add("hello")
        titles.add("Hey")
        contents.add("hello")
    }

    fun noteItemClicked(titles: String, content: String) {
        Toast.makeText(this, "Item Clicked" , Toast.LENGTH_SHORT).show()
    }
    fun fabBtnClicked(view : View){
        startActivity(Intent(this,AddNotes::class.java))
    }

}
