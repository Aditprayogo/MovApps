package com.aditprayogo.movapps.wallet

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import com.aditprayogo.movapps.R
import com.aditprayogo.movapps.utils.Preferences
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_my_wallet_top_up.*

class MyWalletTopUpActivity : AppCompatActivity(), View.OnClickListener {

    private var status10k : Boolean = false
    private var status20k : Boolean = false
    private var status30k : Boolean = false
    private var status100k : Boolean = false
    private var status200k : Boolean = false
    private var status500k : Boolean = false

    private lateinit var mDatabaseRef: DatabaseReference
    private lateinit var mDatabaseRef2: DatabaseReference

    private lateinit var auth: FirebaseAuth
    private lateinit var preferences: Preferences

    private lateinit var etAmount: EditText

    lateinit var sUsername:String

    private var sisaBalance:Int = 0
    private var balance:Int = 0
    private var chooseTopup:Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_wallet_top_up)

        mDatabaseRef2 = FirebaseDatabase.getInstance().getReference()

        etAmount = findViewById(R.id.et_amount)

        preferences = Preferences(this)

        btn_topup_saldo.setOnClickListener(this)

        tv_10k.setOnClickListener(this)
        tv_20k.setOnClickListener(this)
        tv_30k.setOnClickListener(this)
        tv_100k.setOnClickListener(this)
        tv_200k.setOnClickListener(this)
        tv_500k.setOnClickListener(this)
    }

    override fun onClick(v: View) {
        when(v.id) {

            R.id.tv_10k -> {

                if (status10k){

                    disSelect(tv_10k)
                    btn_topup_saldo.visibility = View.INVISIBLE
                    etAmount.setText("")

                } else {
                    //status true
//                    sUsername = preferences.getValues("user").toString()

                    chooseTopup = 10000

                    etAmount.setText("10000")

                    selectMoney(tv_10k)
                    btn_topup_saldo.visibility = View.VISIBLE

                }

            }

            R.id.tv_20k -> {

                if (status20k) {

                    disSelect(tv_20k)
                    btn_topup_saldo.visibility = View.INVISIBLE
                    etAmount.setText("")

                } else {

                    chooseTopup = 20000
                    etAmount.setText("20000")

                    selectMoney(tv_20k)
                    btn_topup_saldo.visibility = View.VISIBLE

                }

            }

            R.id.tv_30k -> {

                if (status30k) {

                    disSelect(tv_30k)
                    btn_topup_saldo.visibility = View.INVISIBLE
                    etAmount.setText("")

                } else {

                    chooseTopup = 30000
                    etAmount.setText("30000")

                    selectMoney(tv_30k)
                    btn_topup_saldo.visibility = View.VISIBLE

                }

            }

            R.id.tv_100k -> {

                if (status100k) {

                    disSelect(tv_100k)
                    btn_topup_saldo.visibility = View.INVISIBLE
                    etAmount.setText("")

                } else {

                    chooseTopup = 100000
                    etAmount.setText("100000")

                    selectMoney(tv_100k)
                    btn_topup_saldo.visibility = View.VISIBLE

                }

            }

            R.id.tv_200k -> {

                if (status200k) {

                    disSelect(tv_200k)
                    btn_topup_saldo.visibility = View.INVISIBLE
                    etAmount.setText("")

                } else {

                    chooseTopup = 20000
                    etAmount.setText("20000")

                    selectMoney(tv_200k)
                    btn_topup_saldo.visibility = View.VISIBLE

                }

            }

            R.id.tv_500k -> {

                if (status500k) {

                    disSelect(tv_500k)
                    btn_topup_saldo.visibility = View.INVISIBLE
                    etAmount.setText("")

                } else {

                    chooseTopup = 500000
                    etAmount.setText("500000")

                    selectMoney(tv_500k)
                    btn_topup_saldo.visibility = View.VISIBLE

                }
            }

            R.id.btn_topup_saldo -> {

                sUsername = preferences.getValues("user").toString()


                mDatabaseRef = FirebaseDatabase.getInstance()
                    .getReference()
                    .child("User")
                    .child(sUsername)

                mDatabaseRef.addListenerForSingleValueEvent(object : ValueEventListener {
                    override fun onCancelled(p0: DatabaseError) {

                    }

                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        //dapetin ballance dari database
                        balance = Integer.valueOf(dataSnapshot.child("saldo")
                            .getValue().toString())
                            .toInt()

                        //nambah value baru ke database
                        sisaBalance = balance + etAmount.text.toString().toInt()

                        //update  value
                        mDatabaseRef.ref.child("saldo").setValue(sisaBalance)

                        val IntentSuccessWallet = Intent(this@MyWalletTopUpActivity,
                            MyWalletSuccessActivity::class.java)

                        startActivity(IntentSuccessWallet)

                        return

                    }

                })



            }

        }
    }

    private fun selectMoney(textView: TextView){
        textView.setTextColor(resources.getColor(R.color.pinkButton))
        textView.setBackgroundResource(R.drawable.shape_line_pink)
        status10k = true
    }

    private fun disSelect(textView: TextView){
        textView.setTextColor(resources.getColor(R.color.white))
        textView.setBackgroundResource(R.drawable.shape_line_white)
        status10k = false
    }

    private fun getUsernameLocal() {
//        SharedPreferences sharedPreference =
    }
}





