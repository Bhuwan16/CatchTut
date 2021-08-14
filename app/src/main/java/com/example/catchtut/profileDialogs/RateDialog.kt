package com.example.catchtut.profileDialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.catchtut.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.rating_bar.view.*
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.math.absoluteValue

class RateDialog:AppCompatDialogFragment() {

    private var rating = -1.0f
    private var rating2 = 0.0f
    private var totalUsers = 1
    private val auth = FirebaseAuth.getInstance()
    lateinit var ref:DatabaseReference

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {


        val uid:String = arguments?.get("uid").toString()
        totalUsers = arguments?.get("totalUsers").toString().toInt()
        Log.d("totalUsers","total1 $totalUsers")
        ref = Firebase
            .database("https://catchtut-12159-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .reference.child("teacher").child(uid)
        ref.child("rating").addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                rating2 = if (snapshot.exists())
                    snapshot.child("totalRating").value.toString().toFloat()
                else
                    0.0f

            }

            override fun onCancelled(error: DatabaseError) {
            }

        })

        var view = LayoutInflater.from(activity).inflate(R.layout.rating_bar,null)
        var dialog = AlertDialog.Builder(context)
        dialog.setTitle("Rate teacher")
        dialog.setView(view)
        view.ratingBar.setOnRatingBarChangeListener { ratingBar, rating, fromUser ->
            this.rating = rating.absoluteValue
        }
        dialog.setPositiveButton("Rate"
        ) { dialog, which ->

            ref.child("rating").child("totalRating")
                .setValue((rating + rating2).toString())
            ref.child("rating").child("users").push().setValue(auth.currentUser.uid)
            Log.d("totalUsers","total2 ${(rating + rating2)}")
            Log.d("totalUsers","total2 ${(rating + rating2)/(totalUsers+1)}")
            ref.child("stars").setValue(((rating + rating2)/(totalUsers+1)).toString())
        }
        dialog.setNegativeButton("cancel"){ dialog, which ->
            dialog.dismiss()
        }

        return dialog.create()

    }

}