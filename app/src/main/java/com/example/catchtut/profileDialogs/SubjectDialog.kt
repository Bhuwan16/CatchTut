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
import kotlinx.android.synthetic.main.edit_subject_dialog.view.*

class SubjectDialog : AppCompatDialogFragment() {

    private val auth = FirebaseAuth.getInstance()
    private var checkSubjectCount = 0
    var subjectMap =  HashMap<String,String>()
    var  subjects = ArrayList<String>()

    private val ref = Firebase
        .database("https://catchtut-12159-default-rtdb.asia-southeast1.firebasedatabase.app/")
        .reference.child("teacher")
        .child(auth.currentUser.uid)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {

        subjects = arguments?.getStringArrayList("subjects") as ArrayList<String>

        var view = LayoutInflater.from(activity).inflate(R.layout.edit_subject_dialog,null)

        if (subjects.contains("All Subjects(for Class I-V)"))
            view.checkAllSubjects.isChecked = true
        if (subjects.contains("English"))
            view.checkEnglish.isChecked = true
        if (subjects.contains("Science(for Class I-VIII)"))
            view.checkScience.isChecked = true
        if (subjects.contains("Mathematics"))
            view.checkMathematics.isChecked = true
        if (subjects.contains("Physics"))
            view.checkPhysics.isChecked = true
        if (subjects.contains("Chemistry"))
            view.checkChemistry.isChecked = true
        if (subjects.contains("Accounts"))
            view.checkAccounts.isChecked = true
        if (subjects.contains("Business"))
            view.checkBusiness.isChecked = true

        var dialog = AlertDialog.Builder(activity)
        dialog.setView(view)
            .setTitle("Select Subjects")
            .setNegativeButton("cancel") {
                    dialog, which ->

            }
            .setPositiveButton("OK"){
                    dialog, which ->
                checkSubjectCheckBox(view)
                if(checkSubjectCount == 0){
                    Toast.makeText(activity,"Subjects not changed. Try again",Toast.LENGTH_SHORT)
                        .show()                }
                else{
                    ref.child("subjectMap").setValue(subjectMap)
                    dialog.dismiss()
                }

            }
        return dialog.create()


    }

    private fun checkSubjectCheckBox(view: View){
        if(view.checkAllSubjects.isChecked){
            checkSubjectCount++
            subjectMap["subject$checkSubjectCount"] = view.checkAllSubjects.text.toString()
        }
        if(view.checkEnglish.isChecked){
            checkSubjectCount++
            subjectMap["subject$checkSubjectCount"] = view.checkEnglish.text.toString()
        }
        if(view.checkScience.isChecked){
            checkSubjectCount++
            subjectMap["subject$checkSubjectCount"] = view.checkScience.text.toString()
        }
        if(view.checkMathematics.isChecked){
            checkSubjectCount++
            subjectMap["subject$checkSubjectCount"] = view.checkMathematics.text.toString()
        }
        if(view.checkPhysics.isChecked){
            checkSubjectCount++
            subjectMap["subject$checkSubjectCount"] = view.checkPhysics.text.toString()
        }
        if(view.checkChemistry.isChecked){
            checkSubjectCount++
            subjectMap["subject$checkSubjectCount"] = view.checkChemistry.text.toString()
        }
        if(view.checkAccounts.isChecked){
            checkSubjectCount++
            subjectMap["subject$checkSubjectCount"] = view.checkAccounts.text.toString()
        }
        if(view.checkBusiness.isChecked){
            checkSubjectCount++
            subjectMap["subject$checkSubjectCount"] = view.checkBusiness.text.toString()
        }
    }
}