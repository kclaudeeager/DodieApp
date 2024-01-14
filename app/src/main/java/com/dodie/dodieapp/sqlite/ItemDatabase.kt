package com.dodie.dodieapp.sqlite

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ItemEntity::class],
    version = 1
)
abstract class ItemDatabase: RoomDatabase() {
    abstract val itemDao: ItemDao

    companion object {
        const val DATABASE_NAME = "item_database"
    }
}