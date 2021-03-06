package com.aditprayogo.movapps.checkout

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.app.NotificationCompat
import androidx.recyclerview.widget.LinearLayoutManager
import com.aditprayogo.movapps.R
import com.aditprayogo.movapps.home.TiketActivity
import com.aditprayogo.movapps.home.model.Film
import com.aditprayogo.movapps.utils.Preferences
import kotlinx.android.synthetic.main.activity_checkout.*
import java.text.NumberFormat
import java.util.*
import kotlin.collections.ArrayList

class CheckoutActivity : AppCompatActivity() {

    private var dataList = ArrayList<Checkout>()
    private var total:Int = 0

    private lateinit var preferences: Preferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checkout)

        preferences = Preferences(this)

        //nangkep data dari pilih bangku
        dataList = intent.getSerializableExtra("data") as ArrayList<Checkout>
        val film = intent.getParcelableExtra<Film>("film")

        for (a in dataList.indices){
            total += dataList[a].harga!!.toInt()
        }

        dataList.add(
            Checkout(
                "Total Harus Dibayar",
                total.toString()
            )
        )

        btn_tiket.setOnClickListener {
            val intent = Intent(this@CheckoutActivity,
                CheckoutSuccessActivity::class.java)
            startActivity(intent)

            showNotif(film)
        }

        rc_checkout.layoutManager = LinearLayoutManager(this)

        //adapter checkout
        rc_checkout.adapter = CheckoutAdapter(dataList) {
        }

        if (preferences.getValues("saldo")!!.isNotEmpty()) {

            val localeID = Locale("in", "ID")
            val formatRupiah = NumberFormat.getCurrencyInstance(localeID)

            tv_saldo.setText(formatRupiah.format(preferences.getValues("saldo")!!.toDouble()))
            btn_tiket.visibility = View.VISIBLE
            tv_error.visibility = View.INVISIBLE

        } else {

            tv_saldo.setText("Rp 0")
            btn_tiket.visibility = View.INVISIBLE
            tv_error.visibility = View.VISIBLE
            tv_error.text = "Saldo Tidak Cukup"

        }
    }

    private fun showNotif(film: Film) {
        val NOTIFICATION_CHANNEL_ID = "channel_bwa_notif"
        val context = this.applicationContext
        var notificationManager = context.getSystemService(NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channelName = "TEST NOTIF CHANNEL"
            val importance = NotificationManager.IMPORTANCE_HIGH

            val mChannel = NotificationChannel(NOTIFICATION_CHANNEL_ID, channelName, importance)
            notificationManager.createNotificationChannel(mChannel)
        }

        val intent = Intent(this, TiketActivity::class.java)
        val bundle = Bundle()
        bundle.putParcelable("data", film)
        intent.putExtras(bundle)

        val pendingIntent =
            PendingIntent.getActivity(this, 0,intent,PendingIntent.FLAG_UPDATE_CURRENT)

        val mBuilder = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
        mBuilder.setContentIntent(pendingIntent)
            .setSmallIcon(R.mipmap.ic_launcher)
            .setLargeIcon(BitmapFactory.decodeResource(this.resources, R.mipmap.ic_launcher))
            .setTicker("Test notif")
            .setAutoCancel(true)
            .setVibrate(longArrayOf(1000,1000,1000,1000,1000))
            .setLights(Color.RED, 3000, 3000)
            .setDefaults(Notification.DEFAULT_SOUND)
            .setContentTitle("Payment was successful")
            .setContentText("Ticket " + film.judul + " has been bought, Enjoy the movie")

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(115, mBuilder.build())
    }
}
