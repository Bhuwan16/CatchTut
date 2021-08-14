package com.example.catchtut.profileDialogs

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatDialogFragment
import com.example.catchtut.R
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.edit_class_dialog.view.*

class ClassDialog: AppCompatDialogFragment() {
    private val auth = FirebaseAuth.getInstance()
    private var cbClassCount = 0
    var classMap =  HashMap<String,String>()
    private val ref = Firebase
        .database("https://catchtut-12159-default-rtdb.asia-southeast1.firebasedatabase.app/")
        .reference.child("teacher")
        .child(auth.currentUser.uid)

    var classes = ArrayList<String>()

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        classes = arguments?.get("classes") as ArrayList<String>


        var view = LayoutInflater.from(activity).inflate(R.layout.edit_class_dialog,null)

        if (classes.contains("I"))
            view.cbClass1.isChecked = true
        if (classes.contains("II"))
            view.cbClass2.isChecked = true
        if (classes.contains("III"))
            view.cbClass3.isChecked = true
        if (classes.contains("IV"))
            view.cbClass4.isChecked = true
        if (classes.contains("V"))
            view.cbClass5.isChecked = true
        if (classes.contains("VI"))
            view.cbClass6.isChecked = true
        if (classes.contains("VII"))
            view.cbClass7.isChecked = true
        if (classes.contains("VIII"))
            view.cbClass8.isChecked = true
        if (classes.contains("IX"))
            view.cbClass9.isChecked = true
        if (classes.contains("X"))
            view.cbClass10.isChecked = true
        if (classes.contains("XI"))
            view.cbClass11.isChecked = true
        if (classes.contains("XII"))
            view.cbClass12.isChecked = true

        var dialog = AlertDialog.Builder(activity)
        dialog.setView(view)
            .setTitle("Select Classes")
            .setNegativeButton("cancel") {
                    dialog, which ->

            }
            .setPositiveButton("OK"){
                    dialog, which ->
                checkClassCheckBox(view)
                if(cbClassCount == 0){
                    Toast.makeText(activity,"Classes not changed. Try again",Toast.LENGTH_SHORT)
                        .show()                }
                else{
                    ref.child("classMap").setValue(classMap)
                    dialog.dismiss()
                }

            }
        return dialog.create()


    }

    private fun checkClassCheckBox(view: View){
        if(view.cbClass1.isChecked){
            cbClassCount++
            classMap["class$cbClassCount"] = view.cbClass1.text.toString()
        }
        if(view.cbClass2.isChecked){
            cbClassCount++
            classMap["class$cbClassCount"] = view.cbClass2.text.toString()
        }
        if(view.cbClass3.isChecked){
            cbClassCount++
            classMap["class$cbClassCount"] = view.cbClass3.text.toString()
        }
        if(view.cbClass4.isChecked){
            cbClassCount++
            classMap["class$cbClassCount"] = view.cbClass4.text.toString()
        }
        if(view.cbClass5.isChecked){
            cbClassCount++
            classMap["class$cbClassCount"] = view.cbClass5.text.toString()
        }
        if(view.cbClass6.isChecked){
            cbClassCount++
            classMap["class$cbClassCount"] = view.cbClass6.text.toString()
        }
        if(view.cbClass7.isChecked){
            cbClassCount++
            classMap["class$cbClassCount"] = view.cbClass7.text.toString()
        }
        if(view.cbClass8.isChecked){
            cbClassCount++
            classMap["class$cbClassCount"] = view.cbClass8.text.toString()
        }
        if(view.cbClass9.isChecked){
            cbClassCount++
            classMap["class$cbClassCount"] = view.cbClass9.text.toString()
        }
        if(view.cbClass10.isChecked){
            cbClassCount++
            classMap["class$cbClassCount"] = view.cbClass10.text.toString()
        }
        if(view.cbClass11.isChecked){
            cbClassCount++
            classMap["class$cbClassCount"] = view.cbClass11.text.toString()
        }
        if(view.cbClass12.isChecked){
            cbClassCount++
            classMap["class$cbClassCount"] = view.cbClass12.text.toString()
        }
    }

}