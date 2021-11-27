package com.devsung.bangull.data

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Customer(
    val name: String,
    val cellPhone: String,
    val homePhone: String,
    val order: String,
    val address: String,
    val time: String
) : Parcelable