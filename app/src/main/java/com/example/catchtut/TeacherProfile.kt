package com.example.catchtut

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.bumptech.glide.Glide
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import kotlinx.android.synthetic.main.activity_teacher_home.*
import kotlinx.android.synthetic.main.activity_teacher_profile.*
import kotlinx.android.synthetic.main.activity_teacher_profile.tvProfileAddress
import kotlinx.android.synthetic.main.activity_teacher_profile.tvProfileClasses
import kotlinx.android.synthetic.main.activity_teacher_profile.tvProfileMode
import kotlinx.android.synthetic.main.activity_teacher_profile.tvProfileName
import kotlinx.android.synthetic.main.activity_teacher_profile.tvProfileSubjects

class TeacherProfile : AppCompatActivity() {

    private val ref = Firebase
        .database("https://catchtut-12159-default-rtdb.asia-southeast1.firebasedatabase.app/")
        .reference.child("teacher")

    private val context = this

    lateinit var phone:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_profile)

        var glide = Glide.with(this)

        val receivedIntent = intent
        val uid = receivedIntent.getStringExtra("TeacherUID")

        ref.child(uid!!).addValueEventListener(object :ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                tvProfileName.text = "${snapshot.child("name").value.toString()} ${snapshot.child("surname").value.toString()}"
                tvProfileMode.text = snapshot.child("mode").value.toString()
                tvProfileAddress.text = "${snapshot.child("area").value.toString()},\n${snapshot.child("city").value.toString()},\n${snapshot.child("state").value.toString()}"
                phone = snapshot.child("phone").value.toString()

                glide.load(snapshot.child("uri").value.toString())
                    .dontTransform()
                    .circleCrop()
                    .placeholder(R.drawable.ic_default)
                    .into(profileImage)
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })

        ref.child(uid!!).child("classMap").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                tvProfileClasses.text = ""
                for(cls in snapshot.children)
                    tvProfileClasses.append(cls.value.toString()+"\n")
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Error! Try again.", Toast.LENGTH_SHORT).show()
            }
        })

        ref.child(uid!!).child("subjectMap").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                tvProfileSubjects.text = ""
                for(sub in snapshot.children)
                    tvProfileSubjects.append(sub.value.toString()+"\n")
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Error! Try again.", Toast.LENGTH_SHORT).show()
            }
        })

        btnReview.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("uid",uid)
            val teacherReviews = TeacherReviews()
            teacherReviews.arguments = bundle
            teacherReviews.show(supportFragmentManager,"Teacher Reviews")
        }

        btnCall.setOnClickListener {
            var uri = "tel:$phone"
            val phoneIntent = Intent(Intent.ACTION_DIAL)
            phoneIntent.data = Uri.parse(uri)
            startActivity(phoneIntent)
        }
    }
}