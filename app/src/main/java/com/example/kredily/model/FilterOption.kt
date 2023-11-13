package com.example.kredily.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FilterOption(
    val type: FilterType,
    val options: List<String>,
    val isActive: Boolean
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FilterOption

        if (type != other.type) return false
        if (options != other.options) return false
        if (isActive != other.isActive) return false

        return true
    }

    override fun hashCode(): Int {
        var result = type.hashCode()
        result = 31 * result + options.hashCode()
        result = 31 * result + isActive.hashCode()
        return result
    }
}