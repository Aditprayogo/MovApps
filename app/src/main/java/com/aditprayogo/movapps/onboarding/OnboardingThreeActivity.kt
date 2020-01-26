package com.aditprayogo.movapps.onboarding

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.aditprayogo.movapps.R
import com.aditprayogo.movapps.sign.signin.SignInActivity
import kotlinx.android.synthetic.main.activity_onboarding_three.*

class OnboardingThreeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_onboarding_three)


        btn_daftar.setOnClickListener {

            finishAffinity()

            val intent =  Intent(this,
                SignInActivity::class.java)
            startActivity(intent)
        }
    }
}
