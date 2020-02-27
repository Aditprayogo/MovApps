package com.aditprayogo.movapps.wallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.aditprayogo.movapps.R
import com.aditprayogo.movapps.wallet.adapter.WalletAdapter
import com.aditprayogo.movapps.wallet.model.Wallet
import kotlinx.android.synthetic.main.activity_my_wallet.*
import java.util.ArrayList

class MyWalletActivity : AppCompatActivity() {

    private var dataList = ArrayList<Wallet>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_wallet)

        dataList.add(
           Wallet(
               "Acip",
               "Sabtu, 13 januari 2020",
               70000.0,
               "1"
           )
        )

        rv_transaction.layoutManager = LinearLayoutManager(this)
        rv_transaction.adapter = WalletAdapter(dataList){}

        btn_topup_saldo.setOnClickListener {
            val intent = Intent(this,MyWalletTopUpActivity::class.java)
            startActivity(intent)
        }


    }
}
