package com.aditprayogo.movapps.home

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.fragment.app.Fragment
import com.aditprayogo.movapps.R
import com.aditprayogo.movapps.home.dashboard.DashboardFragment
import kotlinx.android.synthetic.main.activity_home.*

class HomeActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        val fragmentTiket = TicketFragment()
        val fragmentSetting = SettingFragment()
        val fragmentHome =
            DashboardFragment()

        setFragment(fragmentHome)

        iv_menu1.setOnClickListener {
            setFragment(fragmentHome)

            changeIcon(iv_menu1, R.drawable.home_icon_active)
            changeIcon(iv_menu2, R.drawable.ic_dompet)
            changeIcon(iv_menu3, R.drawable.ic_profile)
        }

        iv_menu2.setOnClickListener {
            setFragment(fragmentTiket)

            changeIcon(iv_menu1, R.drawable.home_icon)
            changeIcon(iv_menu2, R.drawable.ic_dompet_active)
            changeIcon(iv_menu3, R.drawable.ic_profile)
        }

        iv_menu3.setOnClickListener {
            setFragment(fragmentSetting)

            changeIcon(iv_menu1, R.drawable.home_icon)
            changeIcon(iv_menu2, R.drawable.ic_dompet)
            changeIcon(iv_menu3, R.drawable.ic_profile_active)
        }

    }

    protected fun setFragment(fragment: Fragment){

        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.layout_frame,fragment)
        fragmentTransaction.commit()

    }

    private fun changeIcon(imageView: ImageView, int: Int){

        imageView.setImageResource(int)

    }
}
