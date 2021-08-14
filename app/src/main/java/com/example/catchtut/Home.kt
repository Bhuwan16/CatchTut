package com.example.catchtut

import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_home.*

class Home : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        textView.text = auth.currentUser.uid
        /*btnSignOut.setOnClickListener {
            var dialog = AlertDialog.Builder(this)
            dialog.setTitle("Sign Out")
            dialog.setMessage("Are you sure?")
                .setPositiveButton("Yes") {
                        dialog, which ->
                    auth.signOut()
                    val loginIntent = Intent(this,Login::class.java)
                    finish()
                }.setNegativeButton("No") {
                        dialog, which ->
                    dialog.dismiss()
                }
                .show()
        }*/
    }
}