package com.example.catchtut

import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.review.view.*

class ReviewAdapter(var reviews: ArrayList<Review>, var context:Context): RecyclerView.Adapter<ReviewAdapter.MyViewHolder>() {
    class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        var name: TextView = itemView.tvReviewName
        var review: TextView = itemView.tvReview
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        Log.d("ReviewCount","onCreateViewHolder")

        var view = LayoutInflater.from(context).inflate(R.layout.review,parent,false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        Log.d("ReviewCount","onBindViewHolder")

        holder.name.text = reviews[position].name
        holder.review.text = reviews[position].review
    }

    override fun getItemCount(): Int {
        Log.d("ReviewCount",reviews.size.toString())
        return reviews.size
    }
}