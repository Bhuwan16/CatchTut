package com.example.catchtut

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.ActionBar
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.catchtut.profileDialogs.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_teacher_home.*
import kotlinx.android.synthetic.main.edit_photo_dialog.view.*
import kotlinx.android.synthetic.main.teacher_card.*
import kotlin.math.log

class TeacherHome : AppCompatActivity() {

    private val auth = FirebaseAuth.getInstance()

    private val storage = FirebaseStorage.getInstance().reference

    var mName = ""
    var mSurname = ""
    var mMode = ""
    var mPhone = ""
    var mArea = ""
    var mCity = ""
    var mState = ""
    var classes = ArrayList<String>()
    var subjects = ArrayList<String>()

    private val ref = Firebase
        .database("https://catchtut-12159-default-rtdb.asia-southeast1.firebasedatabase.app/")
        .reference.child("teacher")
        .child(auth.currentUser.uid)

    private val context = this

    private val defaultURL= FirebaseStorage.getInstance().reference.child(auth.currentUser.uid).downloadUrl

    override fun onCreate(savedInstanceState: Bundle?) {

        var glide = Glide.with(this)

        Log.d("TeacherHomeLog","created")
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_teacher_home)


        ref.addValueEventListener(object : ValueEventListener{
                override fun onDataChange(snapshot: DataSnapshot) {
                    mName = snapshot.child("name").value.toString()
                    mSurname = snapshot.child("surname").value.toString()
                    mMode = snapshot.child("mode").value.toString()
                    mPhone = snapshot.child("phone").value.toString()
                    mArea = snapshot.child("area").value.toString()
                    mCity = snapshot.child("city").value.toString()
                    mState = snapshot.child("state").value.toString()
                    tvProfileName.text = "$mName $mSurname"
                    tvProfileMode.text = mMode
                    tvProfilePhone.text = mPhone
                    tvProfileAddress.text = "$mArea,\n$mCity,\n$mState"
                    Log.d("GlideTag","In Glide OnClickListener")
                    glide.load(snapshot.child("uri").value.toString())
                        .dontTransform()
                        .circleCrop()
                        .placeholder(R.drawable.ic_default)
                        .into(profile_image)
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context,"Error! Try again.",Toast.LENGTH_SHORT).show()
                }
            })


        ref.child("classMap").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                tvProfileClasses.text = ""
                for(cls in snapshot.children) {
                    classes.add(cls.value.toString())
                    tvProfileClasses.append(cls.value.toString() + "\n")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Error! Try again.",Toast.LENGTH_SHORT).show()
            }
        })

        ref.child("subjectMap").addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                tvProfileSubjects.text = ""
                for(sub in snapshot.children) {
                    subjects.add(sub.value.toString())
                    tvProfileSubjects.append(sub.value.toString() + "\n")
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"Error! Try again.",Toast.LENGTH_SHORT).show()
            }
        })

        btnNameEdit.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("name",mName)
            bundle.putString("surname",mSurname)
            var nameDialog = NameDialog()
            nameDialog.arguments = bundle
            nameDialog.show(supportFragmentManager,"Enter Name")
        }

        btnClassEdit.setOnClickListener {
            var bundle = Bundle()
            bundle.putStringArrayList("classes",classes)
            var classDialog = ClassDialog()
            classDialog.arguments = bundle
            classDialog.show(supportFragmentManager,"Select classes")
        }

        btnSubjectEdit.setOnClickListener {
            var bundle = Bundle()
            bundle.putStringArrayList("subjects",subjects)
            var subjectDialog = SubjectDialog()
            subjectDialog.arguments = bundle
            subjectDialog.show(supportFragmentManager,"Select subjects")
        }

        btnModeEdit.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("mode",mMode)
            var modeDialog = ModeDialog()
            modeDialog.arguments = bundle
            modeDialog.show(supportFragmentManager,"Select Mode")
        }

        btnPhoneEdit.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("phone",mPhone)
            var phoneDialog = PhoneDialog()
            phoneDialog.arguments = bundle
            phoneDialog.show(supportFragmentManager,"Enter phone")
        }

        btnAddressEdit.setOnClickListener {
            var bundle = Bundle()
            bundle.putString("area",mArea)
            bundle.putString("city",mCity)
            bundle.putString("state",mState)
            var addressDialog = AddressDialog()
            addressDialog.arguments = bundle
            addressDialog.show(supportFragmentManager,"Enter address")
        }

        profile_image.setOnClickListener {
            openGallery()
        }

        btnCamera.setOnClickListener {
            openGallery()
        }

    }


    private fun openGallery(){
        val galleryIntent = Intent()
        galleryIntent.action = Intent.ACTION_GET_CONTENT
        galleryIntent.type = "image/*"
        startActivityForResult(galleryIntent,1)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode == 1 && resultCode == RESULT_OK && data != null){
            val uri = data.data
            imageDialog(uri!!)
        }
    }
    private fun imageDialog(uri:Uri){
        var view = LayoutInflater.from(this).inflate(R.layout.edit_photo_dialog, null)
        var dialog = AlertDialog.Builder(this)
        view.profileImage.setImageURI(uri)
        dialog.setView(view)
            .setTitle("Preview")
            .setMessage("Set as Profile image?")
            .setNegativeButton("cancel") { dialog, which ->

            }
            .setPositiveButton("OK") { dialog, which ->
                uploadToFirebase(uri)
            }
            .show()
    }

    private fun uploadToFirebase(uri:Uri) {

        storage.child(auth.currentUser.uid).putFile(uri).addOnCompleteListener() {
            Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show()
            Log.d("GlideTag","Uploaded")

            storage.child(auth.currentUser.uid).downloadUrl.addOnCompleteListener {
                ref.child("uri").setValue(it.result.toString())
                Log.d("GlideTag","uri changed")
            }
            progressProfile.visibility = View.INVISIBLE

        }
            .addOnFailureListener() {
                Toast.makeText(this, it.localizedMessage, Toast.LENGTH_SHORT).show()
                Log.d("storageFirebase","upload canceled")
                progressProfile.visibility = View.INVISIBLE

            }
            .addOnProgressListener(){
                Toast.makeText(this, "Uploading", Toast.LENGTH_SHORT).show()
                Log.d("GlideTag","Uploading")
                progressProfile.visibility = View.VISIBLE
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.teacher_optionsmenu,menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if(id == R.id.teacherLogout){
            var dialog = AlertDialog.Builder(this)
            dialog.setTitle("Logout")
            dialog.setIcon(R.drawable.ic_baseline_exit_to_app_24)
            dialog.setMessage("Are you sure?")
                .setPositiveButton("OK") {
                        dialog, which ->
                    auth.signOut()
                    val loginIntent = Intent(this,Login::class.java)
                    startActivity(loginIntent)
                    finishAffinity()
                    dialog.dismiss()
                }
                .setNegativeButton("cancel"){
                        dialog, which ->
                }
                .show()
        }

        if(id == R.id.teacherDeleteAccount){
            var dialog = AlertDialog.Builder(this)
            dialog.setTitle("Delete Account")
            dialog.setIcon(R.drawable.ic_baseline_delete_24)
            dialog.setMessage("Are you sure?")
                .setPositiveButton("OK") {
                        dialog, which ->
                    ref.removeValue()
                    auth.currentUser.delete()
                    val loginIntent = Intent(this,Login::class.java)
                    startActivity(loginIntent)
                    finishAffinity()
                    dialog.dismiss()
                }
                .setNegativeButton("cancel"){
                    dialog, which ->
                }
                .show()
        }
        return true
    }
}