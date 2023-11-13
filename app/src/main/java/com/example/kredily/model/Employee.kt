package com.example.kredily.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Employee(
    val id: String,
    val firstName: String,
    val lastName: String,
    val profileUrl: String? = null,
    val location: String,
    val department: String,
    val isConfigured: Boolean
) : Parcelable {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Employee

        if (id != other.id) return false
        if (firstName != other.firstName) return false
        if (lastName != other.lastName) return false
        if (profileUrl != other.profileUrl) return false
        if (location != other.location) return false
        if (department != other.department) return false
        if (isConfigured != other.isConfigured) return false

        return true
    }

    override fun hashCode(): Int {
        var result = id.hashCode()
        result = 31 * result + firstName.hashCode()
        result = 31 * result + lastName.hashCode()
        result = 31 * result + (profileUrl?.hashCode() ?: 0)
        result = 31 * result + location.hashCode()
        result = 31 * result + department.hashCode()
        result = 31 * result + isConfigured.hashCode()
        return result
    }
}