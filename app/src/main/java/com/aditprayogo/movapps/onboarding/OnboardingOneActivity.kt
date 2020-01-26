package com.aditprayogo.movapps.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aditprayogo.movapps.R
import com.aditprayogo.movapps.sign.signin.SignInActivity
import com.aditprayogo.movapps.utils.Preferences
import kotlinx.android.synthetic.main.activity_onboarding_one.*


class OnboardingOneActivity : AppCompatActivity() {

    lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_one)

        preferences = Preferences(this)

        if (preferences.getValues("onboarding").equals("1")){
            finishAffinity()

            val intent = Intent(this, SignInActivity::class.java)
            startActivity(intent)

        }

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
