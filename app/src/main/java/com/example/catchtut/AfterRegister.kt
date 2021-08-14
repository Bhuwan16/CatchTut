package com.example.catchtut

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.PopupMenu
import android.widget.RadioButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_after_register.*
import kotlinx.android.synthetic.main.activity_registration.*
import kotlinx.android.synthetic.main.fragment_student_registration.*
import kotlinx.android.synthetic.main.fragment_teacher_registration.*

class AfterRegister : AppCompatActivity(),OnDataPass {
    private var teacherData = TeacherData()
    private var studentData = StudentData()
    private val context = this
    private var className = ""
    private var allET = 0
    private var etCount = 0
    private var cbClassCount = 0
    private var checkSubjectCount = 0
    private val auth = FirebaseAuth.getInstance()
    private val dataBase = Firebase
        .database("https://catchtut-12159-default-rtdb.asia-southeast1.firebasedatabase.app/")
        .reference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_after_register)



        //Log.d("mode", auth.currentUser.getIdToken(false).result?.signInProvider.toString())

        val register = intent
        val isStudent = register.getBooleanExtra("isStudent", true)
        if(isStudent){
            supportFragmentManager.beginTransaction().add(R.id.frameLayout,StudentRegistration()).commit()
        }else{
            supportFragmentManager.beginTransaction().add(R.id.frameLayout,TeacherRegistration()).commit()
        }
        btnSave.setOnClickListener {
            if(isStudent){
                savingForStudent()
                allET = 0
            }else{
                savingForTeacher()
                etCount = 0
                cbClassCount = 0
                checkSubjectCount = 0
            }
        }
    }

    private fun savingForStudent() {
        checkAll()
        if(allET != 4)
            tvErrors.text = "Please fill all details."
        else if(className == "")
            tvErrors.text = "Please select your class."
        else{
            studentData.apply {
                name = etNameS.text.toString()
                surname = etSurnameS.text.toString()
                state = etStateS.text.toString()
                city = etCityS.text.toString()
                classNameS = className
            }
            dataBase.child("student").child(auth.currentUser.uid).setValue(studentData)

            dataBase.child("student").child(auth.currentUser.uid).child("isInfoSaved")
                .setValue("true")

            val studentHomeIntent = Intent(context,StudentHome::class.java)
            studentHomeIntent.flags =
                Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(studentHomeIntent)


        }
    }

    private fun checkAll() {
        if(etNameS.text.toString() != "")
            allET++
        if(etSurnameS.text.toString() != "")
            allET++
        if(etStateS.text.toString() != "")
            allET++
        if(etCityS.text.toString() != "")
            allET++
    }


    private fun checkEditTexts(){
        if(etNameT.text.toString() != "")
            etCount++
        if(etSurnameT.text.toString() != "")
            etCount++
        if(etPhoneT.text.toString() != "")
            etCount++
        if(etStateT.text.toString() != "")
            etCount++
        if(etCityT.text.toString() != "")
            etCount++
        if(etAreaT.text.toString() != "")
            etCount++
    }

    private fun checkClassCheckBox(){
        if(cbClass1.isChecked){
            cbClassCount++
            teacherData.classMap["class$cbClassCount"] = cbClass1.text.toString()
        }
        if(cbClass2.isChecked){
            cbClassCount++
            teacherData.classMap["class$cbClassCount"] = cbClass2.text.toString()
        }
        if(cbClass3.isChecked){
            cbClassCount++
            teacherData.classMap["class$cbClassCount"] = cbClass3.text.toString()
        }
        if(cbClass4.isChecked){
            cbClassCount++
            teacherData.classMap["class$cbClassCount"] = cbClass4.text.toString()
        }
        if(cbClass5.isChecked){
            cbClassCount++
            teacherData.classMap["class$cbClassCount"] = cbClass5.text.toString()
        }
        if(cbClass6.isChecked){
            cbClassCount++
            teacherData.classMap["class$cbClassCount"] = cbClass6.text.toString()
        }
        if(cbClass7.isChecked){
            cbClassCount++
            teacherData.classMap["class$cbClassCount"] = cbClass7.text.toString()
        }
        if(cbClass8.isChecked){
            cbClassCount++
            teacherData.classMap["class$cbClassCount"] = cbClass8.text.toString()
        }
        if(cbClass9.isChecked){
            cbClassCount++
            teacherData.classMap["class$cbClassCount"] = cbClass9.text.toString()
        }
        if(cbClass10.isChecked){
            cbClassCount++
            teacherData.classMap["class$cbClassCount"] = cbClass10.text.toString()
        }
        if(cbClass11.isChecked){
            cbClassCount++
            teacherData.classMap["class$cbClassCount"] = cbClass11.text.toString()
        }
        if(cbClass12.isChecked){
            cbClassCount++
            teacherData.classMap["class$cbClassCount"] = cbClass12.text.toString()
        }
    }

    private fun checkSubjectCheckBox(){
        if(checkAllSubjects.isChecked){
            checkSubjectCount++
            teacherData.subjectMap["subject$checkSubjectCount"] = checkAllSubjects.text.toString()
        }
        if(checkEnglish.isChecked){
            checkSubjectCount++
            teacherData.subjectMap["subject$checkSubjectCount"] = checkEnglish.text.toString()
        }
        if(checkScience.isChecked){
            checkSubjectCount++
            teacherData.subjectMap["subject$checkSubjectCount"] = checkScience.text.toString()
        }
        if(checkMathematics.isChecked){
            checkSubjectCount++
            teacherData.subjectMap["subject$checkSubjectCount"] = checkMathematics.text.toString()
        }
        if(checkPhysics.isChecked){
            checkSubjectCount++
            teacherData.subjectMap["subject$checkSubjectCount"] = checkPhysics.text.toString()
        }
        if(checkChemistry.isChecked){
            checkSubjectCount++
            teacherData.subjectMap["subject$checkSubjectCount"] = checkChemistry.text.toString()
        }
        if(checkAccounts.isChecked){
            checkSubjectCount++
            teacherData.subjectMap["subject$checkSubjectCount"] = checkAccounts.text.toString()
        }
        if(checkBusiness.isChecked){
            checkSubjectCount++
            teacherData.subjectMap["subject$checkSubjectCount"] = checkBusiness.text.toString()
        }
    }

    private fun savingForTeacher(){
        checkEditTexts()
        checkClassCheckBox()
        checkSubjectCheckBox()
        if(etCount != 6)
            tvErrors.text = "Please fill all details."
        else if(etPhoneT.text.toString().length != 10)
            tvErrors.text = "Please enter 10-digit phone number."
        else if(cbClassCount == 0)
            tvErrors.text = "Please select at least one class"
        else if(checkSubjectCount==0)
            tvErrors.text = "Please select at least one subject"
        else{
            teacherData.apply {
                name = etNameT.text.toString()
                surname = etSurnameT.text.toString()
                phone = etPhoneT.text.toString()
                state = etStateT.text.toString()
                city = etCityT.text.toString()
                area = etAreaT.text.toString()
                uid = auth.currentUser.uid
                uri = "https://firebasestorage.googleapis.com/v0/b/catchtut-12159.appspot.com/o/default.png?alt=media&token=c8c351f9-318d-428f-ab54-c97bec0ee131"
                mode = findViewById<RadioButton>(rdGroupMode.checkedRadioButtonId).text.toString()
            }

            dataBase.child("teacher").child(auth.currentUser.uid).setValue(teacherData)

            dataBase.child("teacher").child(auth.currentUser.uid).child("isInfoSaved")
                .setValue("true")



            val teacherHomeIntent = Intent(application,TeacherHome::class.java)
            //teacherHomeIntent.flags =
             //   Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK

            startActivity(teacherHomeIntent)
            finishAffinity()
        }
    }

    override fun onDataPass(data: String) {
        className = data
    }


}