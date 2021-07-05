package com.example.mynotes.model

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mynotes.R
import kotlinx.android.synthetic.main.single_note_view.view.*
import java.util.*

class MyAdapter(private val titles : ArrayList<String>, private val content : ArrayList<String> ,private val listener : noteClicked) : RecyclerView.Adapter<MyViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
       val view = LayoutInflater.from(parent.context).inflate(R.layout.single_note_view,parent,false)
        val holder = MyViewHolder(view)
        view.setOnClickListener {
            listener.noteItemClicked(titles[holder.absoluteAdapterPosition],content[holder.absoluteAdapterPosition])
        }
        return holder
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val currentTitle = titles[position]
        val currentContent = content[position]
        holder.content.text = currentContent
        holder.titles.text = currentTitle
    }

    override fun getItemCount(): Int {
       return titles.size
    }
}
class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
    val content : TextView = itemView.content
    val titles : TextView = itemView.titles
}
interface noteClicked{
    fun noteItemClicked(titles : String,content : String)
}
