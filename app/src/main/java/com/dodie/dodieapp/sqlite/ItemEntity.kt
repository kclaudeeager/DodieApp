package com.dodie.dodieapp.sqlite

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "items")
data class ItemEntity(
    @PrimaryKey val id: Int?=null,
    val title: String,
    val description: String?=null,
    val date: Long?=null,
    val isDeleted: Boolean = false
)
