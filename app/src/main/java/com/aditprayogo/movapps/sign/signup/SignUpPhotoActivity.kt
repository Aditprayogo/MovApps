package com.aditprayogo.movapps.sign.signup

import android.app.Activity
import android.app.ProgressDialog
import android.content.Intent
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
import com.karumi.dexter.PermissionToken
import com.karumi.dexter.listener.PermissionDeniedResponse
import com.karumi.dexter.listener.PermissionGrantedResponse
import com.karumi.dexter.listener.single.PermissionListener
import kotlinx.android.synthetic.main.activity_sign_up_photo.*
import java.util.*

class SignUpPhotoActivity : AppCompatActivity(), PermissionListener {

    val REQUEST_IMAGE_CAPTURE = 1
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
        storageReference = storage.getReference()
        tv_hello.text = "Selamat Datang\n" + intent.getStringExtra("nama")

        iv_add.setOnClickListener {

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

        btn_home.setOnClickListener {

            finishAffinity()
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)

        }

        btn_simpan.setOnClickListener {

            if (filePath != null){

                val progcessDialog = ProgressDialog(this)
                progcessDialog.setTitle("Loading...")
                progcessDialog.show()

                val ref = storageReference.child("images/" + UUID.randomUUID().toString())

                ref.putFile(filePath)
                    .addOnSuccessListener {
                        progcessDialog.dismiss()
                        Toast.makeText(this, "Uploaded", Toast.LENGTH_LONG).show()

                        ref.downloadUrl.addOnSuccessListener {
                            preferences.setValues("url", it.toString())
                        }

                        finishAffinity()
                        val intent = Intent(this,
                            HomeActivity::class.java)
                        startActivity(intent)
                    }
                    .addOnFailureListener { e ->
                        progcessDialog.dismiss()
                        Toast.makeText(this, "Failed " + e.message, Toast.LENGTH_LONG).show()
                    }
                    .addOnProgressListener { taskSnapshot ->
                        val progcess = 100.0 * taskSnapshot.bytesTransferred / taskSnapshot.totalByteCount
                        progcessDialog.setMessage("Uploaded " + progcess.toInt() + "%")
                    }

            }
        }
    }

    override fun onPermissionGranted(response: PermissionGrantedResponse?) {

        Intent(MediaStore.ACTION_IMAGE_CAPTURE).also { takePictureIntent ->
            takePictureIntent.resolveActivity(packageManager)?.also {
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE)
            }
        }
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
            //Image Uri will not be null for RESULT_OK
            filePath = data?.data!!

            Glide.with(this)
                .load(filePath)
                .apply(RequestOptions.circleCropTransform())
                .into(iv_profile)

            Log.v("tampan", "File uploaded" +filePath)

            btn_simpan.visibility = View.VISIBLE
            iv_add.setImageResource(R.drawable.ic_btn_upload)


        } else if (resultCode == ImagePicker.RESULT_ERROR) {
            Toast.makeText(this, ImagePicker.getError(data), Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(this, "Task Cancelled", Toast.LENGTH_SHORT).show()
        }
    }
}
