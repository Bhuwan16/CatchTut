package com.example.catchtut

import android.app.Activity
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.PopupMenu
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import com.example.catchtut.databinding.ActivityRegistrationBinding
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.activity_main.view.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.activity_registration.view.*


class Registration : AppCompatActivity() {

    private val dataBase = Firebase.database("https://catchtut-12159-default-rtdb.asia-southeast1.firebasedatabase.app/").reference
    private val auth = FirebaseAuth.getInstance()
    val context = this
    var email = ""
    var password = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registration)



        btnRegister.setOnClickListener {

            email = findViewById<TextInputLayout>(R.id.etEmailR).editText?.text.toString()
            password = findViewById<TextInputLayout>(R.id.etPassR).editText?.text.toString()

            if (email == "" || password == "") {
                tvError.text = "Please fill both above fields"
            } else {
                auth.createUserWithEmailAndPassword(
                    email,
                    password
                )
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            if (radioStudent.isChecked)
                                dataBase.child("student").child(auth.currentUser.uid)
                                    .child("isInfoSaved").setValue("false")
                            else
                                dataBase.child("teacher").child(auth.currentUser.uid)
                                    .child("isInfoSaved").setValue("false")



                            val afterRegisterIntent = Intent(this, AfterRegister::class.java)
                            afterRegisterIntent.putExtra("isStudent", radioStudent.isChecked)
                            startActivity(afterRegisterIntent)
                            //updateUI(user)
                        }
                    }.addOnFailureListener {
                        var dialog = AlertDialog.Builder(this)
                        dialog.setTitle("Login Error")
                        dialog.setIcon(R.drawable.ic_baseline_error_24)
                        dialog.setMessage(it.localizedMessage)
                            .setPositiveButton("OK") { dialog, which ->
                                dialog.dismiss()
                            }
                            .show()
                    }
            }

        }

    }

}