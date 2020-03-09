package com.aditprayogo.movapps

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import com.aditprayogo.movapps.home.HomeActivity
import com.aditprayogo.movapps.home.SettingFragment
import com.aditprayogo.movapps.utils.Preferences
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.MultiplePermissionsReport
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.PermissionRequest
import com.karumi.dexter.listener.multi.MultiplePermissionsListener
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_edit_profile.*
import kotlinx.android.synthetic.main.activity_edit_profile.btn_home
import kotlinx.android.synthetic.main.activity_edit_profile.iv_add
import kotlinx.android.synthetic.main.activity_sign_up_photo.*
import kotlinx.android.synthetic.main.scan_qr_dialog.view.*
import java.util.*
import java.util.jar.Manifest

class EditProfileActivity : AppCompatActivity(), View.OnClickListener, PermissionListener {



    private lateinit var preferences: Preferences
    lateinit var sUsername: String
    lateinit var sUser: String

    var filePath: Uri? = null

    var statusAdd:Boolean = false

    //firebase stuff
    private lateinit var mDatabaseRef: DatabaseReference
    private lateinit var mFirebaseInstance: FirebaseDatabase
    private lateinit var mDatabase: DatabaseReference
    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_profile)

        preferences = Preferences(this)

        storage = FirebaseStorage.getInstance()
        storageReference = storage.getReference()

        mFirebaseInstance = FirebaseDatabase.getInstance()
        mDatabase = FirebaseDatabase.getInstance().getReference()

        sUsername = preferences.getValues("user").toString()

//        val fragmentSetting = SettingFragment()
        
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
        iv_add.setOnClickListener(this)


    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {
        ImagePicker.with(this)
            .cameraOnly()
            .start()
    }

    override fun onPermissionRationaleShouldBeShown(
        permission: PermissionRequest?,
        token: PermissionToken?
    ) {

    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Toast.makeText(this,"Anda tidak bisa menambahkan photo profile", Toast.LENGTH_LONG).show()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {

        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == Activity.RESULT_OK) {

            statusAdd = true
            //Image Uri will not be null for RESULT_OK
            filePath = data?.data!!

            Glide.with(this)
                .load(filePath)
                .apply(RequestOptions.circleCropTransform())
                .into(image_user)

            iv_add.setImageResource(R.drawable.ic_btn_upload)

        } else if (resultCode == ImagePicker.RESULT_ERROR) {

            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()

        } else {

            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()

        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.btn_home -> {

                val progressDialog = ProgressDialog(this)
                progressDialog.setTitle("Uploading..")
                progressDialog.show()

                if (filePath != null) {
                    val ref = storageReference.child("images/" + UUID.randomUUID().toString()).child(sUsername)

                    ref.putFile(filePath!!)
                        .addOnSuccessListener {
                            progressDialog.dismiss()
                            Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show()
//                            val result = it.metadata!!.reference!!.downloadUrl
                            // dapetin url dari firebase storage
                            ref.downloadUrl.addOnSuccessListener  {

                                preferences.setValues("url", it.toString())

                                val imageLink = it.toString()

                                sUsername = preferences.getValues("user").toString()

                                mDatabase
                                    .child("User")
                                    .child(sUsername)
                                    .child("url")
                                    .setValue(imageLink)

                            }
                        }
                        .addOnFailureListener { e ->
                            progressDialog.dismiss()
                            Toast.makeText(this,"Error :" + e.message, Toast.LENGTH_SHORT).show()

                        }
                        .addOnProgressListener {taskSnapshot ->
                            val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot
                                .totalByteCount
                            progressDialog.setMessage("Uploaded " + progress.toInt() + "%")

                        }

                    progressDialog.setTitle("Loading")
                    progressDialog.show()

                }

                    mDatabaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onCancelled(p0: DatabaseError) {

                        }
                        override fun onDataChange(dataSnapshot: DataSnapshot) {

                            mDatabaseRef.ref.child("email").setValue(et_email.text.toString())
                            mDatabaseRef.ref.child("nama").setValue(et_nama.text.toString())
                            mDatabaseRef.ref.child("password").setValue(et_password.text.toString())
                            mDatabaseRef.ref.child("username").setValue(et_username.text.toString())

                            Toast.makeText(this@EditProfileActivity, "User Updated", Toast.LENGTH_SHORT).show()

                            progressDialog.dismiss()
                        }
                    })


            }

            R.id.iv_back -> {

                finish()

            }
            
            R.id.iv_add -> {
                if (statusAdd){

                    statusAdd = false
                    btn_simpan.visibility = View.VISIBLE
                    iv_add.setImageResource(R.drawable.btn_upload)
                    iv_profile.setImageResource(R.drawable.user_pic)

                } else {

                    ImagePicker.with(this)
                        .cameraOnly()
                        .start()

                }
            }
        }
    }



}


