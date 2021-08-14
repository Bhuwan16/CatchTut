package com.example.catchtut.profileDialogs

import android.app.AlertDialog
import android.app.Dialog
import android.content.DialogInterface
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.RadioButton
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.catchtut.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.edit_mode_dialog.view.*

class ModeDialog: AppCompatDialogFragment() {

    private val auth = FirebaseAuth.getInstance()
    private val ref = Firebase
        .database("https://catchtut-12159-default-rtdb.asia-southeast1.firebasedatabase.app/")
        .reference.child("teacher")
        .child(auth.currentUser.uid)

    var mMode = ""

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        mMode = arguments?.getString("mode")!!

        var view = LayoutInflater.from(activity).inflate(R.layout.edit_mode_dialog, null)
        if (mMode == "Offline")
            view.radioOffline.isChecked = true
        else if (mMode == "Online")
            view.radioOnline.isChecked = true
        else
            view.radioBoth.isChecked = true
        var dialog = AlertDialog.Builder(activity)
        dialog.setView(view)
            .setTitle("Select Mode")
            .setNegativeButton("cancel") { dialog, which ->
            }
            .setPositiveButton("OK") { dialog, which ->
                val mode = view
                    .findViewById<RadioButton>(view.rdGroupMode.checkedRadioButtonId)
                    .text.toString()

                ref.child("mode").setValue(mode)
            }

        return dialog.create()

    }
}