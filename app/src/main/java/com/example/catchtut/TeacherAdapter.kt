package com.example.catchtut

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView
import kotlinx.android.synthetic.main.teacher_card.view.*

class TeacherAdapter(var context: Context , var teachers :
ArrayList<TeachData>) : RecyclerView.Adapter<TeacherAdapter.TeacherViewHolder>() {

    lateinit var listener: OnItemClickListener

    interface OnItemClickListener{
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener){
        this.listener = listener
    }

    class TeacherViewHolder(itemView: View, listener: OnItemClickListener) : RecyclerView.ViewHolder(itemView) {

        var name: TextView = itemView.nameCard
        var subjects: TextView = itemView.subjectsCard
        var classes: TextView = itemView.classesCard
        var image: CircleImageView = itemView.profileImageCard
        var mode: TextView = itemView.modeCard
        var rating: TextView = itemView.rating

        init {
            itemView.setOnClickListener{
                if(listener != null){
                    val position = adapterPosition
                    if (position != RecyclerView.NO_POSITION){
                        listener.onItemClick(position)
                    }
                }
            }
        }

    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TeacherViewHolder {
        var view = LayoutInflater.from(context).inflate(R.layout.teacher_card,parent,false)
        return TeacherViewHolder(view,listener)
    }

    override fun getItemCount(): Int {
        return teachers.size
    }

    override fun onBindViewHolder(holder: TeacherViewHolder, position: Int) {
        /*holder.name.text = teachers[position].name
        holder.surname.text = teachers[position].surname
        holder.state.text = teachers[position].state
        holder.city.text = teachers[position].city*/

        Picasso.get()
            .load(teachers[position].uri)
            .placeholder(R.drawable.ic_default)
            .into(holder.image)

        holder.name.text = teachers[position].name+" "+teachers[position].surname

        if(teachers[position].stars == "")
            holder.rating.text = "N/A"
        else {
            if(teachers[position].stars.length > 1)
                holder.rating.text = teachers[position].stars.subSequence(0,3)
            else
                holder.rating.text = teachers[position].stars+".0"
        }

        if(teachers[position].mode == "Both")
            holder.mode.text = "Online/Offline"
        else
            holder.mode.text = teachers[position].mode

        holder.subjects.text =""

        for(s in teachers[position].subjectMap)
            holder.subjects.append(s.value+"\n")

        val size = teachers[position].classMap.size
        var count = 0

        holder.classes.text = ""

        for(c in teachers[position].classMap){
            if(count==size-1)
                holder.classes.append(c.value)
            else
                holder.classes.append(c.value+", ")
            count++
        }

    }
}