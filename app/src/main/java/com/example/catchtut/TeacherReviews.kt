package com.example.catchtut

import android.app.AlertDialog
import android.content.DialogInterface
import android.os.Bundle
import android.renderscript.Sampler
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.catchtut.profileDialogs.RateDialog
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_teacher_reviews.*
import kotlinx.android.synthetic.main.rating_bar.*
import kotlin.math.absoluteValue

class TeacherReviews : BottomSheetDialogFragment() {

    var reviews = ArrayList<Review>()
    lateinit var review: Review

    private val auth = FirebaseAuth.getInstance()
    var isRated = false
    var users = ArrayList<String>()
    lateinit var ref:DatabaseReference
    lateinit var name:String
    lateinit var surname:String
    private var sendingReview = Review()
    lateinit var uid:String

    var ref2 = Firebase
    .database("https://catchtut-12159-default-rtdb.asia-southeast1.firebasedatabase.app/")
    .reference.child("student").child(auth.currentUser.uid)

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        uid = arguments?.get("uid").toString()
        Log.d("ReviewCount",uid)


        ref = Firebase
            .database("https://catchtut-12159-default-rtdb.asia-southeast1.firebasedatabase.app/")
            .reference.child("teacher").child(uid)
        return inflater.inflate(R.layout.fragment_teacher_reviews, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        btnSend.visibility = View.INVISIBLE


        var reviewAdapter = ReviewAdapter(reviews,context!!)

        recyclerReviews.adapter = reviewAdapter
        recyclerReviews.layoutManager = LinearLayoutManager(context)

        ref.child("reviews").addChildEventListener(object: ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if(snapshot.exists()){
                    review = snapshot.getValue(Review::class.java)!!
                    reviews.add(review)
                    Log.d("ReviewCount",review.name)
                    reviewAdapter.notifyDataSetChanged()
                }
                else
                    Log.d("ReviewCount","else")
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onChildRemoved(snapshot: DataSnapshot) {}

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {}

            override fun onCancelled(error: DatabaseError) {}
        })

        etReview.addTextChangedListener(object :TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(s: Editable?) {
                if (s != null) {
                    if (s.isNotEmpty() && s.trim().isNotEmpty())
                        btnSend.visibility = View.VISIBLE
                    else
                        btnSend.visibility = View.INVISIBLE

                }
            }

        })

        ref2.addListenerForSingleValueEvent(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                name = snapshot.child("name").value.toString()
                surname = snapshot.child("surname").value.toString()
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        btnSend.setOnClickListener {
            users.clear()
            ref.child("rating").addListenerForSingleValueEvent(object :ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    if (snapshot.exists()){
                        for (i in snapshot.child("users").children){
                            users.add(i.getValue(String::class.java)!!)
                            Log.d("rating",i.getValue(String::class.java)!!)
                        }
                        if (users.contains(auth.currentUser.uid)){

                        }
                        else{
                            var bundle = Bundle()
                            bundle.putString("uid",uid)
                            bundle.putInt("totalUsers",users.size)
                            val rateDialog = RateDialog()
                            rateDialog.arguments = bundle
                            rateDialog.show(requireFragmentManager(),"ok")
                        }

                    }
                    else{
                        var bundle = Bundle()
                        bundle.putString("uid",uid)
                        val rateDialog = RateDialog()
                        rateDialog.arguments = bundle
                        bundle.putInt("totalUsers",users.size)
                        rateDialog.show(requireFragmentManager(),"ok")
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                }
            })
            sendingReview.name = "$name $surname"
            sendingReview.review = etReview.text.toString()
            ref.child("reviews").push().setValue(sendingReview)
            etReview.setText("", TextView.BufferType.EDITABLE)
            Toast.makeText(context,"Review added",Toast.LENGTH_SHORT).show()
        }
    }
}