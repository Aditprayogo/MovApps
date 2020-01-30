package com.aditprayogo.movapps.home


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager

import com.aditprayogo.movapps.R
import com.aditprayogo.movapps.home.adapter.ComingSoonAdapter
import com.aditprayogo.movapps.home.model.Film
import com.aditprayogo.movapps.utils.Preferences
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.fragment_ticket.*

/**
 * A simple [Fragment] subclass.
 */
class TicketFragment : Fragment() {

    private lateinit var preferences: Preferences
    lateinit var databaseRef: DatabaseReference

    private var dataList = ArrayList<Film>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_ticket, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {

        super.onActivityCreated(savedInstanceState)

        preferences = Preferences(activity!!.applicationContext)
        databaseRef = FirebaseDatabase.getInstance().getReference("Film")

        rc_tiket.layoutManager = LinearLayoutManager(context!!.applicationContext)
        getData()

    }

    private fun getData() {

        databaseRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {

                //di clear dulu datalistnya
                dataList.clear()

                for (getdataSnapshot in dataSnapshot.getChildren()) {

                    val film = getdataSnapshot.getValue(Film::class.java!!)

                    //di masukin ke datalist film
                    dataList.add(film!!)
                }

                rc_tiket.adapter = ComingSoonAdapter(dataList) {
                    val intent = Intent(context, TiketActivity::class.java)
                        .putExtra("data", it)
                    startActivity(intent)
                }

                tv_total.setText(dataList.size.toString() +" Movies")

            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, ""+error.message, Toast.LENGTH_LONG).show()
            }
        })
    }



}
