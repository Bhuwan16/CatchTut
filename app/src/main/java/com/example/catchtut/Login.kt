package com.example.catchtut

import android.app.AlertDialog
import android.app.Dialog
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.btnLogin
import kotlinx.android.synthetic.main.activity_main.etEmail
import kotlinx.android.synthetic.main.activity_main.etPassword
import kotlinx.android.synthetic.main.activity_main.tvBottom
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_registration.view.*

class Login : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()
    private val dataBase = Firebase.database("https://catchtut-12159-default-rtdb.asia-southeast1.firebasedatabase.app/")
        .reference
    private var isInfoSaved = "false"
    private var visited = false
    private val context = this
    var email = ""
    var password = ""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        tvBottom.setOnClickListener {
            val registrationIntent = Intent(this,Registration::class.java)
            startActivity(registrationIntent)
        }

        btnLogin.setOnClickListener {

            email = findViewById<TextInputLayout>(R.id.etEmail).editText?.text.toString()
            password = findViewById<TextInputLayout>(R.id.etPassword).editText?.text.toString()

            if(email == "" || password == "")
                tvErrorLogin.text = "Please enter both Email and Password."
            else{
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            checkIsInfoSaved()
                        }
                    }.addOnFailureListener {
                        var dialog = AlertDialog.Builder(this)
                        dialog.setTitle("Login Error")
                        dialog.setIcon(R.drawable.ic_baseline_error_24)
                        dialog.setMessage(it.localizedMessage)
                            .setPositiveButton("OK") {
                                    dialog, which ->
                                dialog.dismiss()
                            }
                            .show()
                    }
            }
        }
    }
    private fun checkIsInfoSaved(){
        val progressDialog = ProgressDialog(context)
        progressDialog.setMessage("Loading")
        progressDialog.show()
        dataBase.child("student").child(auth.currentUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener{

            override fun onCancelled(error: DatabaseError) {}

            override fun onDataChange(snapshot: DataSnapshot) {
                Log.d("Login","studentcallback")
                if(snapshot.exists()){
                    isInfoSaved = snapshot.child("isInfoSaved").value.toString()
                    if(isInfoSaved == "true"){
                        Log.d("Login","studenttrue")

                        Toast.makeText(context,"OK",Toast.LENGTH_SHORT).show()
                        val studentHomeIntent = Intent(context,StudentHome::class.java)
                        progressDialog.dismiss()
                        startActivity(studentHomeIntent)
                        finish()
                    }else if(isInfoSaved == "false" && !visited){
                        Log.d("Login","studentfalse")
                        visited = true
                        progressDialog.dismiss()
                        val afterRegisterIntent = Intent(context,AfterRegister::class.java)
                        afterRegisterIntent.putExtra("isStudent",true)
                        startActivity(afterRegisterIntent)
                    }
                }else{
                    itsTeacher(progressDialog)
                }
            }
        })


    }

    private fun itsTeacher(progressDialog: ProgressDialog) {
        dataBase.child("teacher").child(auth.currentUser.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener{

                override fun onCancelled(error: DatabaseError) {}

                override fun onDataChange(snapshot: DataSnapshot) {

                    Log.d("Login","teachercallback")


                    isInfoSaved = snapshot.child("isInfoSaved").value.toString()
                    if(isInfoSaved == "true"){
                        Log.d("Login","teachertrue")

                        Toast.makeText(context,"true",Toast.LENGTH_SHORT).show()
                        val teacherHomeIntent = Intent(context,TeacherHome::class.java)
                        progressDialog.dismiss()
                        startActivity(teacherHomeIntent)
                        finish()
                    }else if(isInfoSaved == "false"){
                        Log.d("Login","teacherfalse")
                        Log.d("TeacherVisited",visited.toString())
                        progressDialog.dismiss()
                        val afterRegisterIntent = Intent(context,AfterRegister::class.java)
                        afterRegisterIntent.putExtra("isStudent",false)
                        startActivity(afterRegisterIntent)
                        Toast.makeText(context,"Teacher",Toast.LENGTH_SHORT).show()
                    }

                }
            })
    }
}