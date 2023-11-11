package com.example.kredily.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Contact(
    val email: String,
    val dialCode: String,
    val mobile: String
) : Parcelable
