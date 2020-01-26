package com.aditprayogo.movapps.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aditprayogo.movapps.R
import com.aditprayogo.movapps.sign.signin.SignInActivity
import kotlinx.android.synthetic.main.activity_onboarding_one.*


class OnboardingOneActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_one)

        btn_lanjut.setOnClickListener {

            val intent = Intent(this, OnboardingTwoActivity::class.java)
            startActivity(intent)

        }

        btn_lewati.setOnClickListener {

            finishAffinity()
            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)

        }

    }





}
