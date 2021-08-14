package com.example.catchtut.profileDialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.inputmethod.InputMethodManager
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.catchtut.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.edit_name_dialog.view.*


class NameDialog : AppCompatDialogFragment() {

    private val auth = FirebaseAuth.getInstance()

    private val ref = Firebase
        .database("https://catchtut-12159-default-rtdb.asia-southeast1.firebasedatabase.app/")
        .reference.child("teacher")
        .child(auth.currentUser.uid)

    var mName = ""
    var mSurname = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        mName = arguments?.getString("name")!!
        mSurname = arguments?.getString("surname")!!

        var view = LayoutInflater.from(activity).inflate(R.layout.edit_name_dialog,null)
        view.etEditProfileName.setText(mName,TextView.BufferType.EDITABLE)
        view.etEditProfileName.setSelectAllOnFocus(true)
        view.etEditProfileSurname.setText(mSurname,TextView.BufferType.EDITABLE)
        view.etEditProfileSurname.setSelectAllOnFocus(true)
        var dialog = AlertDialog.Builder(activity)
        dialog.setView(view)
            .setTitle("Edit name")
            .setNegativeButton("cancel") {
                    dialog, which ->

            }
            .setPositiveButton("OK"){
                    dialog, which ->
                if(view.etEditProfileName.text.toString() == ""
                    || view.etEditProfileSurname.text.toString() == ""){

                    Toast.makeText(activity,"Name not changed. Try again", Toast.LENGTH_SHORT)
                        .show()                }else{
                    ref.child("name").setValue(view.etEditProfileName.text.toString())
                    ref.child("surname").setValue(view.etEditProfileSurname.text.toString())
                    dialog.dismiss()
                }
            }
        return dialog.create()
    }

}

