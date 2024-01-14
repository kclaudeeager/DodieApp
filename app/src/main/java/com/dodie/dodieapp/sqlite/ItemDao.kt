package com.dodie.dodieapp.sqlite
import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ItemDao {
 // Define all the functions for CRUD operations

    @Query("SELECT * FROM items WHERE isDeleted = 0")
    fun getAllItems(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE id = :id")
    fun getItemById(id: Int): Flow<ItemEntity>

    @Upsert
    suspend fun insertItem(item: ItemEntity)

    @Update
    suspend fun updateItem(item: ItemEntity)

    @Query("UPDATE items SET isDeleted = 1 WHERE id = :id")
    suspend fun deleteItem(id: Int)

    @Delete
    suspend fun deleteItem(item: ItemEntity)

    // Add a function to delete all items
    @Query("DELETE FROM items")
    suspend fun deleteAllItems()

    // get all items ordered by date
    @Query("SELECT * FROM items WHERE isDeleted = 0 ORDER BY date DESC")
    fun getAllItemsOrderedByDate(): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items ORDER BY date DESC LIMIT :loadSize OFFSET :position")
     fun loadItems(position: Int, loadSize: Int): Flow<List<ItemEntity>>

    @Query("SELECT * FROM items WHERE title LIKE :searchQuery ORDER BY title ASC LIMIT :limit OFFSET :offset")
     fun searchItems(searchQuery: String, offset: Int, limit: Int): Flow<List<ItemEntity>>
}
