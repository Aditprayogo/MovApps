package com.aditprayogo.movapps.wallet.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Wallet(
    var title: String ?="",
    var data: String ?="",
    var money: Double ,
    var status: String ?=""

) : Parcelable