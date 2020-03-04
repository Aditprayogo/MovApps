package com.aditprayogo.movapps.wallet

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.aditprayogo.movapps.R
import com.aditprayogo.movapps.home.HomeActivity
import kotlinx.android.synthetic.main.activity_my_wallet_success.*

class MyWalletSuccessActivity : AppCompatActivity(), View.OnClickListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_wallet_success)

        btn_home.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        when(v?.id){
            R.id.btn_home -> {
                val intent = Intent(this,HomeActivity::class.java)
                startActivity(intent)
            }

        }
    }
}
