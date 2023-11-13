package com.example.kredily.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class Setting(
    @DrawableRes val icon: Int,
    val title: String,
    val type: Type
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Setting

        if (icon != other.icon) return false
        if (title != other.title) return false
        if (type != other.type) return false

        return true
    }

    override fun hashCode(): Int {
        var result = icon
        result = 31 * result + title.hashCode()
        result = 31 * result + type.hashCode()
        return result
    }

    enum class Type {
        TYPE_SWITCH_CAMERA,
        TYPE_RESET_PASSCODE
    }
}