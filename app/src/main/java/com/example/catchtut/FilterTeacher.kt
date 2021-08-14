package com.example.catchtut

import android.content.Context
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.annotation.RequiresApi
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.fragment_filter_teacher.*

class FilterTeacher : BottomSheetDialogFragment(){

    private val ref = Firebase
        .database("https://catchtut-12159-default-rtdb.asia-southeast1.firebasedatabase.app/")
        .reference.child("teacher")

    private val auth = FirebaseAuth.getInstance()


    lateinit var v:RadioButton
    lateinit var dataPasser: PassData
    var filterData = FilterData()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_filter_teacher, container, false)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as PassData
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        rdGroupMode.setOnCheckedChangeListener { group, checkedId ->
            filterData.mode = view.findViewById<RadioButton>(checkedId).text.toString()
        }

        rdGroupSubjects.setOnCheckedChangeListener{ group, checkedId ->
            filterData.subject = view.findViewById<RadioButton>(checkedId).text.toString()
        }



        btnOK.setOnClickListener {
            if(etCity.text.trim().isEmpty())
                filterData.city = "-1"
            else
                filterData.city = etCity.text.toString()

            filterData.classes = view
                .findViewById<RadioButton>(rdGroupClasses.checkedRadioButtonId)
                .text.toString()

            pass(filterData)

            dismiss()
        }
    }

    private fun pass(filterData: FilterData){
        dataPasser.onDataPass(filterData)
    }
}

interface PassData{
    fun onDataPass(filterData: FilterData)
}