package com.example.catchtut

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.PopupMenu
import kotlinx.android.synthetic.main.fragment_student_registration.*


class StudentRegistration : Fragment() {
    private lateinit var popup: PopupMenu
    private var className = ""
    lateinit var dataPasser: OnDataPass

    override fun onAttach(context: Context) {
        super.onAttach(context)
        dataPasser = context as OnDataPass
    }
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_student_registration, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        popup = PopupMenu(context,btnClass)
        popup.menuInflater.inflate(R.menu.class_menu,popup.menu)

        btnClass.setOnClickListener {
            popup.show()
        }

        popup.setOnMenuItemClickListener {
            className = it.title.toString()
            passData(className)
            tvSelectedClass.text = className
            true
        }

    }
    fun passData(data: String){
        dataPasser.onDataPass(data)
    }
}
interface OnDataPass {
    fun onDataPass(data: String)
}

