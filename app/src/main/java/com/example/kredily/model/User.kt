package com.example.kredily.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class User(
    val id: Int,
    val firstName: String,
    val lastName: String,
    val contact: Contact,
    val password: String,
    val otp: String
) : Parcelable