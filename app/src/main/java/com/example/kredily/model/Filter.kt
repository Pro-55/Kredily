package com.example.kredily.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Filter(
    val type: FilterType,
    val option: String
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Filter

        if (type != other.type) return false
        if (option != other.option) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + option.hashCode()
        return result
    }
}