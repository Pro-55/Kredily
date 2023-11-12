package com.example.kredily.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserOffices(
    val userId: Int,
    val offices: List<String>
) : Parcelable