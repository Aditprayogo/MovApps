package com.aditprayogo.movapps.wallet

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aditprayogo.movapps.R
import com.aditprayogo.movapps.utils.Preferences
import kotlinx.android.synthetic.main.activity_my_wallet.*

class MyWalletActivity : AppCompatActivity() {

    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_wallet)

        preferences = Preferences(this)


    }
}
