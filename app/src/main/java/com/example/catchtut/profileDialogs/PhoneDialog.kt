package com.example.catchtut.profileDialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.catchtut.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.edit_phone_dialog.view.*

class PhoneDialog : AppCompatDialogFragment(){
    private val auth = FirebaseAuth.getInstance()

    private val ref = Firebase
        .database("https://catchtut-12159-default-rtdb.asia-southeast1.firebasedatabase.app/")
        .reference.child("teacher")
        .child(auth.currentUser.uid)

    var mPhone = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        mPhone = arguments?.getString("phone")!!

        var view = LayoutInflater.from(activity).inflate(R.layout.edit_phone_dialog,null)

        view.etEditProfilePhone.setText(mPhone,TextView.BufferType.EDITABLE)
        view.etEditProfilePhone.setSelectAllOnFocus(true)

        var dialog = AlertDialog.Builder(activity)
        dialog.setView(view)
            .setTitle("Enter Phone")
            .setNegativeButton("cancel") {
                    dialog, which ->

            }
            .setPositiveButton("OK"){
                    dialog, which ->
                if(view.etEditProfilePhone.text.toString().length != 10){
                    Toast.makeText(activity,"Phone not changed. Try again",Toast.LENGTH_SHORT)
                        .show()
                }else{
                    ref.child("phone").setValue(view.etEditProfilePhone.text.toString())
                    dialog.dismiss()
                }
            }
        return dialog.create()
    }
}
