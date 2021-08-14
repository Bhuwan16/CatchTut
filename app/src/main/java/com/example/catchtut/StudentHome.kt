package com.example.catchtut

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.Icon
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_student_home.*
import kotlinx.android.synthetic.main.activity_teacher_home.*
import kotlinx.android.synthetic.main.teacher_card.*
import kotlin.math.abs

class StudentHome : AppCompatActivity(),PassData {

    val auth = FirebaseAuth.getInstance()
    var id:String = ""

    private var taskbarID = 0
    lateinit var view: View

    var totalTeachers = ArrayList<TeachData>()
    var teacher2 = ArrayList<TeachData>()
    var key = ArrayList<String>()
    var myAdapter = TeacherAdapter(this,totalTeachers)
    var myAdapter2 = TeacherAdapter(this,teacher2)

    private val ref = Firebase
        .database("https://catchtut-12159-default-rtdb.asia-southeast1.firebasedatabase.app/")
        .reference.child("teacher")

    private val ref2 = Firebase
        .database("https://catchtut-12159-default-rtdb.asia-southeast1.firebasedatabase.app/")
        .reference.child("student").child(auth.currentUser.uid).child("city")

    val context = this

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_student_home)

        var value:TeachData
        studentRecycler.layoutManager = LinearLayoutManager(context)
        studentRecycler.adapter = myAdapter

        ref.addChildEventListener(object : ChildEventListener {
            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                if(snapshot.exists()){
                    if(snapshot.child("isInfoSaved").value.toString() == "true"){
                        value = snapshot.getValue(TeachData::class.java)!!
                        key.add(snapshot.key!!)
                        totalTeachers.add(value)
                        myAdapter.notifyDataSetChanged()
                    }
                }
                else
                    Toast.makeText(context,"Currently no data", Toast.LENGTH_SHORT).show()
            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                var keyy = snapshot.key
                var index = key.indexOf(keyy)
                totalTeachers.removeAt(index)
                key.removeAt(index)
                myAdapter.notifyDataSetChanged()
            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {
                TODO("Not yet implemented")
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }
        })




        myAdapter.setOnItemClickListener(object : TeacherAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                var teacherProfileIntent = Intent(context,TeacherProfile::class.java)
                teacherProfileIntent.putExtra("TeacherUID",totalTeachers[position].uid.toString())
                startActivity(teacherProfileIntent)
            }
        })

        myAdapter2.setOnItemClickListener(object : TeacherAdapter.OnItemClickListener{
            override fun onItemClick(position: Int) {
                var teacherProfileIntent = Intent(context,TeacherProfile::class.java)
                teacherProfileIntent.putExtra("TeacherUID",teacher2[position].uid)
                startActivity(teacherProfileIntent)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

       menuInflater.inflate(R.menu.student_optionsmenu,menu)
        menu?.findItem(R.id.btnClrFilter)?.isVisible = taskbarID != 0
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == R.id.studentLogout) {
            var dialog = AlertDialog.Builder(this)
            dialog.setTitle("Logout")
            dialog.setIcon(R.drawable.ic_baseline_exit_to_app_24)
            dialog.setMessage("Are you sure?")
                .setPositiveButton("OK") { dialog, which ->
                    auth.signOut()
                    val loginIntent = Intent(this, Login::class.java)
                    startActivity(loginIntent)
                    finishAffinity()
                    dialog.dismiss()
                }
                .setNegativeButton("cancel") { dialog, which ->
                }
                .show()
        }else if (id == R.id.btnFilter){
            var filterTeacher = FilterTeacher()
            filterTeacher.show(supportFragmentManager,"Filter Teacher")
        }
        else if (id == R.id.btnClrFilter){
            studentRecycler.adapter = myAdapter
            taskbarID = 0
            invalidateOptionsMenu()
        }
        return true
    }

    override fun onDataPass(filterData: FilterData) {

        if(teacher2.size != 0)
            teacher2.clear()

        taskbarID = 1
        invalidateOptionsMenu()

        if (filterData.city == "-1" && filterData.mode == "" && filterData.subject == ""){
            for (i in totalTeachers){
                if (i.classMap.containsValue(filterData.classes)){
                    teacher2.add(i)
                }
            }
            studentRecycler.adapter = myAdapter2
        }
        else if (filterData.city == "-1" && filterData.mode == "" && filterData.subject != ""){
            for(i in totalTeachers){
                if (i.subjectMap.containsValue(filterData.subject)
                    && i.classMap.containsValue(filterData.classes)){
                    teacher2.add(i)
                }
            }
            studentRecycler.adapter = myAdapter2
        }
        else if (filterData.city == "-1" && filterData.mode != "" && filterData.subject == ""){
            if (filterData.mode == "Both"){
                for(i in totalTeachers){
                    if (i.classMap.containsValue(filterData.classes)){
                        teacher2.add(i)
                    }
                }
            }else{
                for(i in totalTeachers){
                    if (i.mode == filterData.mode
                        && i.classMap.containsValue(filterData.classes)){
                        teacher2.add(i)
                    }
                }
            }
            studentRecycler.adapter = myAdapter2
        }
        else if (filterData.city == "-1" && filterData.mode != "" && filterData.subject != ""){
            if (filterData.mode == "Both"){
                for(i in totalTeachers){
                    if (i.subjectMap.containsValue(filterData.subject)
                        && i.classMap.containsValue(filterData.classes)){
                        teacher2.add(i)
                    }
                }
            }else{
                for(i in totalTeachers){
                    if (i.mode == filterData.mode
                        && i.subjectMap.containsValue(filterData.subject)
                        && i.classMap.containsValue(filterData.classes)){
                        teacher2.add(i)
                    }
                }
            }
            studentRecycler.adapter = myAdapter2
        }
        else if (filterData.city != "-1" && filterData.mode == "" && filterData.subject == ""){
            for(i in totalTeachers){
                if (i.city == filterData.city
                    && i.classMap.containsValue(filterData.classes)){
                    teacher2.add(i)
                }
            }
            studentRecycler.adapter = myAdapter2
        }
        else if (filterData.city != "-1" && filterData.mode == "" && filterData.subject != ""){
            for(i in totalTeachers){
                if (i.city == filterData.city
                    && i.subjectMap.containsValue(filterData.subject)
                    && i.classMap.containsValue(filterData.classes)){
                    teacher2.add(i)
                }
            }
            studentRecycler.adapter = myAdapter2
        }
        else if (filterData.city != "-1" && filterData.mode != "" && filterData.subject == ""){
            if (filterData.mode == "Both"){
                for(i in totalTeachers){
                    if (i.city == filterData.city
                        && i.classMap.containsValue(filterData.classes)){
                        teacher2.add(i)
                    }
                }
            }else{
                for(i in totalTeachers){
                    if (i.mode == filterData.mode
                        && i.city == filterData.city
                        && i.classMap.containsValue(filterData.classes)){
                        teacher2.add(i)
                    }
                }
            }
            studentRecycler.adapter = myAdapter2
        }
        else if (filterData.city != "-1" && filterData.mode != "" && filterData.subject != ""){
            if (filterData.mode == "Both"){
                for(i in totalTeachers){
                    if (i.city == filterData.city
                        && i.subjectMap.containsValue(filterData.subject)
                        && i.classMap.containsValue(filterData.classes)){
                        teacher2.add(i)
                    }
                }
            }else{
                for(i in totalTeachers){
                    if (i.mode == filterData.mode
                        && i.city == filterData.city
                        && i.subjectMap.containsValue(filterData.subject)
                        && i.classMap.containsValue(filterData.classes)){
                        teacher2.add(i)
                    }
                }
            }
            studentRecycler.adapter = myAdapter2
        }
        myAdapter2.notifyDataSetChanged()
    }
}