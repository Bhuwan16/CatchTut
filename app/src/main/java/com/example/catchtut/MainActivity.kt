package com.example.catchtut
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_main.*
class MainActivity : AppCompatActivity() {
    private val auth = FirebaseAuth.getInstance()
    private val dataBase = Firebase.database("https://catchtut-12159-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
    private var isInfoSaved:String = "true"
    private val context = this
    private var visited = false
    private var visited2 = false

    override fun onStart() {
        super.onStart()

        Log.d("visited",visited.toString())

        if(auth.currentUser != null){
            checkIsInfoSaved()
        }else{
            val loginIntent = Intent(this,Login::class.java)
            startActivity(loginIntent)
        }
    }

    private fun checkIsInfoSaved(){
        var progressDialog = ProgressDialog(this)
        progressDialog.setMessage("Loading")
        progressDialog.show()
        dataBase.child("student").child(auth.currentUser.uid)
            .addValueEventListener(object : ValueEventListener{
            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    isInfoSaved = snapshot.child("isInfoSaved").value.toString()
                    if(isInfoSaved == "true"){
                        progressDialog.dismiss()
                        val studentHomeIntent = Intent(context,StudentHome::class.java)
                        startActivity(studentHomeIntent)
                    }else{
                        progressDialog.dismiss()
                        val loginIntent = Intent(context,Login::class.java)
                        startActivity(loginIntent)
                    }
                }else{
                    itsTeacher(progressDialog)
                }

            }
        })
    }

    private fun itsTeacher(progressDialog:ProgressDialog) {
        dataBase.child("teacher").child(auth.currentUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener{
                override fun onCancelled(error: DatabaseError) {}

                override fun onDataChange(snapshot: DataSnapshot) {
                    isInfoSaved = snapshot.child("isInfoSaved").value.toString()
                    if(isInfoSaved == "true"){
                        progressDialog.dismiss()
                        val teacherHomeIntent = Intent(context,TeacherHome::class.java)
                        startActivity(teacherHomeIntent)
                    }else if(isInfoSaved == "false"){
                        progressDialog.dismiss()
                        val loginIntent = Intent(context,Login::class.java)
                        startActivity(loginIntent)
                    }
                }
            })
    }
}