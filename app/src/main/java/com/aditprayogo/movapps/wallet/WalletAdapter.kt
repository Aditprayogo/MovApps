package com.aditprayogo.movapps.wallet

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.aditprayogo.movapps.R
import java.text.NumberFormat
import java.util.*

class WalletAdapter(private var data: List<Wallet>,
                    private val listener: (Wallet) -> Unit)
    : RecyclerView.Adapter<WalletAdapter.LeagueViewHolder>() {

    lateinit var ContextAdapter : Context

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LeagueViewHolder {

        val layoutInflater = LayoutInflater.from(parent.context)

        ContextAdapter = parent.context

        val inflatedView: View = layoutInflater.inflate(R.layout.row_item_transaction, parent, false)

        return LeagueViewHolder(
            inflatedView
        )
    }

    override fun onBindViewHolder(holder: LeagueViewHolder, position: Int) {
        holder.bindItem(data[position], listener, ContextAdapter, position)
    }

    override fun getItemCount(): Int = data.size

    class LeagueViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val tvMovie: TextView = view.findViewById(R.id.tv_movie)
        private val tvDate: TextView = view.findViewById(R.id.tv_date)
        private val tvMoney: TextView = view.findViewById(R.id.tv_money)

        fun bindItem(data: Wallet, listener: (Wallet) -> Unit, context : Context, position : Int) {

            tvDate.text = data.date
            tvMovie.text = data.title

            val localID = Locale("in", "ID")
            val formatRupiah = NumberFormat.getCurrencyInstance(localID)

            if (data.status.equals("0")) {
                tvMoney.text = "- "+formatRupiah.format(data.money)
            } else {
                tvMoney.text = "+ "+formatRupiah.format(data.money)
                tvMoney.setTextColor(Color.GREEN)
            }




            itemView.setOnClickListener {
                listener(data)
            }
        }

    }

}

