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
import kotlinx.android.synthetic.main.edit_address_dialog.view.*

class AddressDialog: AppCompatDialogFragment() {
    private val auth = FirebaseAuth.getInstance()

    private val ref = Firebase
        .database("https://catchtut-12159-default-rtdb.asia-southeast1.firebasedatabase.app/")
        .reference.child("teacher")
        .child(auth.currentUser.uid)

    var mArea = ""
    var mCity = ""
    var mState = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        mArea = arguments?.getString("area")!!
        mCity = arguments?.getString("city")!!
        mState = arguments?.getString("state")!!

        var view = LayoutInflater.from(activity).inflate(R.layout.edit_address_dialog,null)

        view.etEditProfileSector.setText(mArea,TextView.BufferType.EDITABLE)
        view.etEditProfileSector.setSelectAllOnFocus(true)
        view.etEditProfileCity.setText(mCity,TextView.BufferType.EDITABLE)
        view.etEditProfileCity.setSelectAllOnFocus(true)
        view.etEditProfileState.setText(mState,TextView.BufferType.EDITABLE)
        view.etEditProfileState.setSelectAllOnFocus(true)

        var dialog = AlertDialog.Builder(activity)
        dialog.setView(view)
            .setTitle("Enter Address")
            .setNegativeButton("cancel") {
                    dialog, which ->

            }
            .setPositiveButton("OK"){
                    dialog, which ->
                if(view.etEditProfileState.text.toString() == ""
                    || view.etEditProfileCity.text.toString() == ""
                    || view.etEditProfileSector.text.toString() == ""){

                    Toast.makeText(activity,"Address not changed. Try again", Toast.LENGTH_SHORT)
                        .show()
                }else{
                    ref.child("state").setValue(view.etEditProfileState.text.toString())
                    ref.child("city").setValue(view.etEditProfileCity.text.toString())
                    ref.child("area").setValue(view.etEditProfileSector.text.toString())
                    dialog.dismiss()
                }
            }
        return dialog.create()
    }
}
