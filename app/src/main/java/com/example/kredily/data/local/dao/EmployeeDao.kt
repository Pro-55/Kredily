package com.example.kredily.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.kredily.model.local.EntityEmployee

@Dao
interface EmployeeDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(employees: List<EntityEmployee>): List<Long>

    @Query("SELECT * FROM employee_table")
    suspend fun getAll(): List<EntityEmployee>

    @Query("DELETE FROM employee_table")
    fun deleteAll()
}