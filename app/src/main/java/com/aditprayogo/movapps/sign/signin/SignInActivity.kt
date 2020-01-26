package com.aditprayogo.movapps.sign.signin

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.aditprayogo.movapps.HomeActivity
import com.aditprayogo.movapps.R
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_sign_in.*


class SignInActivity : AppCompatActivity() {

    lateinit var  iUsername :String
    lateinit var  iPassword :String

    lateinit var mDatabase :DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)

        mDatabase = FirebaseDatabase.getInstance().getReference("User")

        btn_masuk.setOnClickListener {
            iUsername = et_username.text.toString()
            iPassword = et_password.text.toString()

            if (iUsername.equals("")){

                et_username.error = "Silahkan tulis username anda"
                et_username.requestFocus()

            } else if (iPassword.equals("")){

                et_password.error = "Silahkan masukan password anda"
                et_password.requestFocus()

            } else {

                pushlogin(iUsername,iPassword)

            }
        }
    }

    private fun pushlogin(iUsername: String, iPassword: String){
        mDatabase.child(iUsername).addValueEventListener(object : ValueEventListener {
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@SignInActivity, ""+error.message, Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {

                val user = dataSnapshot.getValue(User::class.java)

                if (user == null) {

                    Toast.makeText(this@SignInActivity,"User Tidak Di Temukan", Toast.LENGTH_LONG).show()

                } else {

                    if (user.password.equals(iPassword)) {

                        Toast.makeText(this@SignInActivity, "Selamat Datang", Toast.LENGTH_LONG).show()

//                        preferences.setValues("nama", user.nama.toString())
//                        preferences.setValues("user", user.username.toString())
//                        preferences.setValues("url", user.url.toString())
//                        preferences.setValues("email", user.email.toString())
//                        preferences.setValues("saldo", user.saldo.toString())
//                        preferences.setValues("status", "1")



                        val intent = Intent(this@SignInActivity, HomeActivity::class.java)
                        startActivity(intent)

                    } else {
                        Toast.makeText(this@SignInActivity, "Password anda salah", Toast.LENGTH_LONG).show()
                    }

                }
            }
        })
    }


}
