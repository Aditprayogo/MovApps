package com.aditprayogo.movapps.sign.signup

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.Toast
import com.aditprayogo.movapps.home.HomeActivity
import com.aditprayogo.movapps.R
import com.aditprayogo.movapps.utils.Preferences
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.karumi.dexter.Dexter
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_sign_up_photo.*
import java.util.*

class SignUpPhotoActivity : AppCompatActivity(), PermissionListener, View.OnClickListener {

    var statusAdd:Boolean = false
    lateinit var filePath: Uri

    lateinit var storage: FirebaseStorage
    lateinit var storageReference: StorageReference
    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up_photo)

        preferences = Preferences(this)
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();

        tv_hello.text = "Selamat Datang\n" + intent.getStringExtra("nama")

        iv_add.setOnClickListener(this)

        btn_home.setOnClickListener(this)

        btn_simpan.setOnClickListener(this)

    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {

        ImagePicker.with(this)
            .cameraOnly()
            .start()
    }

    override fun onPermissionRationaleShouldBeShown(
        permission: com.karumi.dexter.listener.PermissionRequest?,
        token: PermissionToken?
    )
    {

    }

    override fun onPermissionDenied(response: PermissionDeniedResponse?) {
        Toast.makeText(this,"Anda tidak bisa menambahkan photo profile", Toast.LENGTH_LONG).show()
    }

    override fun onBackPressed() {
        Toast.makeText(this,"Tergesah?Klik tombol nanti saja", Toast.LENGTH_LONG).show()
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
                .into(iv_profile)

            btn_simpan.visibility = View.VISIBLE
            iv_add.setImageResource(R.drawable.ic_btn_upload)

        } else if (resultCode == ImagePicker.RESULT_ERROR) {

            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()

        } else {

            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()

        }
    }

    override fun onClick(v: View) {
        when(v.id){
            R.id.iv_add -> {
                if (statusAdd){

                    statusAdd = false
                    btn_simpan.visibility = View.VISIBLE
                    iv_add.setImageResource(R.drawable.btn_upload)
                    iv_profile.setImageResource(R.drawable.user_pic)

                } else {

                    ImagePicker.with(this)
                        .galleryOnly()
                        .start()

                }
            }

            R.id.btn_home -> {
                finishAffinity()
                val intent = Intent(this, HomeActivity::class.java)
                startActivity(intent)
            }

            R.id.btn_simpan -> {
                if (filePath != null) {

                    val progressDialog = ProgressDialog(this)
                    progressDialog.setTitle("Uploading...")
                    progressDialog.show()

                    val ref = storageReference.child("images/" + UUID.randomUUID().toString())

                    ref.putFile(filePath)
                        .addOnSuccessListener {
                            progressDialog.dismiss()
                            Toast.makeText(this, "Uploaded", Toast.LENGTH_SHORT).show()

                            // dapetin url dari firebase storage
                            ref.downloadUrl.addOnSuccessListener  {
                                preferences.setValues("url", it.toString())

                                Log.v("tamvan", "url"+it.toString())

                                // pindah ke next activity
                                finishAffinity()
                                val intent = Intent(this,
                                    HomeActivity::class.java)
                                startActivity(intent)
                            }


                        }
                        .addOnFailureListener { e ->
                            progressDialog.dismiss()
                            Toast.makeText(this, "Failed " + e.message, Toast.LENGTH_SHORT)
                                .show()
                        }
                        .addOnProgressListener { taskSnapshot ->
                            val progress = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot
                                .totalByteCount
                            progressDialog.setMessage("Uploaded " + progress.toInt() + "%")
                        }
                }
            }
        }
    }
}
