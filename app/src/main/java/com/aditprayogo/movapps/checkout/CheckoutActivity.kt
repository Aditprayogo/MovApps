package com.aditprayogo.movapps.checkout

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.aditprayogo.movapps.R
import com.aditprayogo.movapps.checkout.model.Checkout
import com.aditprayogo.movapps.utils.Preferences
import kotlinx.android.synthetic.main.activity_checkout.*
import kotlinx.android.synthetic.main.fragment_setting.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class CheckoutActivity : AppCompatActivity() {

    private var dataList = ArrayList<Checkout>()
    private var total:Int = 0

    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        preferences = Preferences(this)

        //nangkep data dari pilih bangku
        dataList = intent.getSerializableExtra("data") as ArrayList<Checkout>


        for (a in dataList.indices){
            total += dataList[a].harga!!.toInt()
        }

        dataList.add(Checkout("Total Harus Dibayar", total.toString()))

        btn_tiket.setOnClickListener {
            val intent = Intent(this@CheckoutActivity,
                CheckoutSuccessActivity::class.java)
            startActivity(intent)
        }

        rc_checkout.layoutManager = LinearLayoutManager(this)

        //adapter checkout
        rc_checkout.adapter = CheckoutAdapter(dataList) {

        }

        if (preferences.getValues("saldo")!!.isNotEmpty()) {

            val localeID = Locale("in", "ID")
            val formatRupiah = NumberFormat.getCurrencyInstance(localeID)

            tv_saldo.setText(formatRupiah.format(preferences.getValues("saldo")!!.toDouble()))
            btn_tiket.visibility = View.VISIBLE
            tv_error.visibility = View.INVISIBLE

        } else {

            tv_saldo.setText("Rp 0")
            btn_tiket.visibility = View.INVISIBLE
            tv_error.visibility = View.VISIBLE
            tv_error.text = "Saldo Tidak Cukup"

        }


    }
}
