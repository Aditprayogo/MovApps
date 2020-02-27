package com.aditprayogo.movapps.sign.signup

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import com.aditprayogo.movapps.R
import com.aditprayogo.movapps.sign.model.User
import com.aditprayogo.movapps.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_up.*

class SignUpActivity : AppCompatActivity(), View.OnClickListener {

    lateinit var sUsername:String
    lateinit var sPassword:String
    lateinit var sNama:String
    lateinit var sEmail:String

    private lateinit var mFirebaseDatabase: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    private lateinit var mDatabase: DatabaseReference

    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        mFirebaseInstance = FirebaseDatabase.getInstance()

        mDatabase = FirebaseDatabase.getInstance().getReference()

        mFirebaseDatabase = mFirebaseInstance.getReference("User")

        preferences = Preferences(this)

        btn_lanjut.setOnClickListener(this)


    }

    private fun saveUser(sUsername: String, sPassword: String, sNama: String, sEMail: String){

        val user = User()
        user.username = sUsername
        user.password = sPassword
        user.nama = sNama
        user.email = sEMail

        if (sUsername != null) {

            checkingUsername(sUsername, user)
        }

    }

    private fun checkingUsername(iUsername: String, data: User){

        mFirebaseDatabase.child(iUsername).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val user = dataSnapshot.getValue(User::class.java)

                if (user == null){

                    mFirebaseDatabase.child(iUsername).setValue(data)

                    preferences.setValues("nama", data.nama.toString())
                    preferences.setValues("user", data.username.toString())
                    preferences.setValues("url", "")
                    preferences.setValues("email", data.email.toString())
                    preferences.setValues("status", "1")

                    val intent = Intent(this@SignUpActivity,
                        SignUpPhotoActivity::class.java).putExtra("nama", data.nama)
                        startActivity(intent)

                }else {

                    Toast.makeText(this@SignUpActivity,
                        "User sudah digunakan", Toast.LENGTH_LONG).show()

                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignUpActivity, ""+error.message, Toast.LENGTH_LONG).show()
            }


        })


    }

    override fun onClick(v: View) {
        when(v.id) {

            R.id.btn_lanjut -> {
                sUsername = et_username.text.toString()
                sPassword = et_password.text.toString()
                sNama = et_nama.text.toString()
                sEmail = et_email.text.toString()

                if (sUsername.equals("")){

                    et_username.error = "Silahkan isi username"
                    et_username.requestFocus()

                }else if (sPassword.equals("")){

                    et_password.error = "Silahkan isi password"
                    et_password.requestFocus()

                }else if(sNama.equals("")){

                    et_nama.error = "Tolong isi nama"
                    et_nama.requestFocus()

                }else if(sEmail.equals("")){

                    et_email.error = "Tolong isi Alamat email"
                    et_email.requestFocus()

                } else {

                    var statusUsername = sUsername.indexOf(".")

                    if (statusUsername >= 0) {

                        et_username.error = "Username cant contain ."
                        et_username.requestFocus()

                    } else {
                        saveUser(sUsername,sPassword,sNama, sEmail)
                    }


                }
            }


        }
    }
}
