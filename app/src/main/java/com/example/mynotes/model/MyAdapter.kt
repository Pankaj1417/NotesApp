package com.example.mynotes.model

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.NotesDeatils
import com.example.mynotes.R
import kotlinx.android.synthetic.main.single_note_view.view.*
import java.util.*
import kotlin.collections.ArrayList

class MyAdapter(private val notes: ArrayList<Notes> ) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.single_note_view,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentTitle = notes[position].title
        val currentContent = notes[position].title
        holder.content.text = currentContent
        holder.titles.text = currentTitle
        holder.view.setOnClickListener {
            val intent : Intent = Intent(holder.view.context,NotesDeatils::class.java)
            intent.putExtra("Title" , currentTitle)
            intent.putExtra("Content",currentContent)
            holder.view.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return notes.size
    }
}
class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val content : TextView = itemView.content
    val titles : TextView = itemView.titles
     val view : View = itemView
}