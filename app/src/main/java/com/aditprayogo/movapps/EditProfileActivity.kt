package com.aditprayogo.movapps

import android.app.ProgressDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.aditprayogo.movapps.home.SettingFragment
import com.aditprayogo.movapps.utils.Preferences
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.scan_qr_dialog.view.*

class EditProfileActivity : AppCompatActivity(), View.OnClickListener {


    private lateinit var mDatabaseRef: DatabaseReference

    private lateinit var preferences: Preferences

    lateinit var sUsername: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        preferences = Preferences(this)

        sUsername = preferences.getValues("user").toString()

        val fragmentSetting = SettingFragment()

        mDatabaseRef = FirebaseDatabase.getInstance()
            .reference
            .child("User")
            .child(sUsername)

        mDatabaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                et_username.setText(dataSnapshot.child("username").getValue().toString())
                et_password.setText(dataSnapshot.child("password").getValue().toString())
                et_nama.setText(dataSnapshot.child("nama").getValue().toString())
                et_email.setText(dataSnapshot.child("email").getValue().toString())

                Glide.with(this@EditProfileActivity)
                    .load(dataSnapshot.child("url").getValue().toString())
                    .apply(RequestOptions.circleCropTransform())
                    .into(image_user)
            }

        })

        btn_home.setOnClickListener(this)
        iv_back.setOnClickListener(this)


    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_home -> {

                mDatabaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {

                        mDatabaseRef.ref.child("email").setValue(et_email.text.toString())
                        mDatabaseRef.ref.child("nama").setValue(et_nama.text.toString())
                        mDatabaseRef.ref.child("password").setValue(et_password.text.toString())
                        mDatabaseRef.ref.child("username").setValue(et_username.text.toString())

                        val progressDialog = ProgressDialog(this@EditProfileActivity)

                        progressDialog.setTitle("Loading")
                        progressDialog.show()

                        Toast.makeText(this@EditProfileActivity, "User Updated", Toast.LENGTH_SHORT).show()

                        progressDialog.dismiss()
                    }

                })



            }

            R.id.iv_back -> {

                finish()

            }
        }
    }

}
