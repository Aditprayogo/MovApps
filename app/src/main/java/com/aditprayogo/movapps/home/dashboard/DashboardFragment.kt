package com.aditprayogo.movapps.home.dashboard


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.aditprayogo.movapps.home.DetailActivity

import com.aditprayogo.movapps.R
import com.aditprayogo.movapps.home.adapter.ComingSoonAdapter
import com.aditprayogo.movapps.home.adapter.NowPlayingAdapter
import com.aditprayogo.movapps.home.model.Film
import com.aditprayogo.movapps.utils.Preferences
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_dashboard.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

/**
 * A simple [Fragment] subclass.
 */
class DashboardFragment : Fragment() {

    private lateinit var preferences: Preferences

    lateinit var databaseRef: DatabaseReference
    lateinit var databaseRef2: DatabaseReference

    lateinit var sUsername: String

    private var dataList = ArrayList<Film>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        preferences = Preferences(activity!!.applicationContext)
        databaseRef = FirebaseDatabase.getInstance().getReference("Film")

        sUsername = preferences.getValues("user").toString()

        databaseRef2 = FirebaseDatabase.getInstance()
            .getReference()
            .child("User")
            .child(sUsername)
        databaseRef2.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onCancelled(p0: DatabaseError) {

            }

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                tv_nama.text = dataSnapshot.child("nama").getValue().toString()

                // kalo data ada
                if (!dataSnapshot.child("saldo").getValue().toString().equals("")) {
                    currecy(dataSnapshot.child("saldo").getValue().toString().toDouble(), tv_saldo)
                }

//                tv_saldo.text = dataSnapshot.child("saldo").getValue().toString()
            }

        })

//        tv_nama.setText(preferences.getValues("nama"))

        //kalo saldo kosong
//        if (!preferences.getValues("saldo").equals("")){
//            currecy(preferences.getValues("saldo")!!.toDouble(), tv_saldo)
//        }

        Glide.with(this)
            .load(preferences.getValues("url"))
            .apply(RequestOptions.circleCropTransform())
            .into(iv_profile)

        rv_now_playing.layoutManager = LinearLayoutManager(context,
            LinearLayoutManager.HORIZONTAL, false)

        rv_coming_soon.layoutManager = LinearLayoutManager(context!!.applicationContext)

        getData()

    }

    private fun getData(){
        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                dataList.clear()

                for (getdataSnapshot in dataSnapshot.getChildren()) {

                    val film = getdataSnapshot.getValue(Film::class.java!!)
                    dataList.add(film!!)
                }

                rv_now_playing.adapter = NowPlayingAdapter(dataList) {
                        val intent = Intent(
                            context,
                            DetailActivity::class.java
                        ).putExtra("data", it)
                        startActivity(intent)
                }

                rv_coming_soon.adapter = ComingSoonAdapter(dataList) {

                        val intent = Intent(
                            context,
                            DetailActivity::class.java
                        ).putExtra("data", it)
                        startActivity(intent)
                }

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }

    private fun currecy(harga: Double, textView: TextView) {

        val localeID = Locale("in", "ID")
        val formatRupiah = NumberFormat.getCurrencyInstance(localeID)
        textView.setText(formatRupiah.format(harga as Double))

    }


}
