package com.aditprayogo.movapps.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import androidx.appcompat.app.AlertDialog
import androidx.recyclerview.widget.LinearLayoutManager
import com.aditprayogo.movapps.R
import com.aditprayogo.movapps.checkout.Checkout
import com.aditprayogo.movapps.home.adapter.TiketAdapter
import com.aditprayogo.movapps.home.model.Film
import com.bumptech.glide.Glide
import kotlinx.android.synthetic.main.activity_tiket.*
import kotlinx.android.synthetic.main.scan_qr_dialog.view.*

class TiketActivity : AppCompatActivity() {

    // naruh data dari checkout
    private var dataList = ArrayList<Checkout>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_tiket)

        val data = intent.getParcelableExtra<Film>("data")

        tv_title.text = data.judul
        tv_genre.text = data.genre
        tv_rate.text = data.rating

        Glide.with(this)
            .load(data.poster)
            .into(iv_poster_image)

        //barcode
        iv_barcode.setOnClickListener {
            val qrDialogView = LayoutInflater.from(this).inflate(R.layout.scan_qr_dialog, null)
            //AlertDialogBuilder
            val mBuilder = AlertDialog.Builder(this)
                .setView(qrDialogView)
                .setTitle("Scan QR Code")
            //show dialog
            val  mAlertDialog = mBuilder.show()
            qrDialogView.btn_cancel.setOnClickListener {
                mAlertDialog.dismiss()
            }
        }

        rc_checkout.layoutManager = LinearLayoutManager(this)
        dataList.add(Checkout("C1", ""))
        dataList.add(Checkout("C2", ""))
        rc_checkout.adapter =
            TiketAdapter(dataList) {
            }
    }
}
