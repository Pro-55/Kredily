package com.example.kredily.model.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.kredily.model.Employee

@Entity(tableName = "employee_table")
data class EntityEmployee(

    @PrimaryKey(autoGenerate = false)
    val id: String,

    val firstName: String,

    val lastName: String,

    val profileUrl: String? = null,

    val location: String,

    val department: String,

    val isConfigured: Boolean
)

fun List<EntityEmployee?>.parse(): List<Employee> = mapNotNull { it?.parse() }

fun EntityEmployee.parse(): Employee = Employee(
    id = id,
    firstName = firstName,
    lastName = lastName,
    profileUrl = profileUrl,
    location = location,
    department = department,
    isConfigured = isConfigured
)