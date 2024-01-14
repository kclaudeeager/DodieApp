package com.dodie.dodieapp
import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.cachedIn
import androidx.room.Room
import com.dodie.dodieapp.sqlite.ItemDatabase
import com.dodie.dodieapp.sqlite.ItemEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch

class ListViewModel(application: Application) : ViewModel() {
    private val database = Room.databaseBuilder(
        application,
        ItemDatabase::class.java,
        ItemDatabase.DATABASE_NAME
    ).build()

    private val itemDao = database.itemDao

    // Use a SharedFlow to signal a data refresh
    private val dataRefreshFlow = MutableSharedFlow<Unit>()
    private val itemSearchFlow = MutableStateFlow<String?>(null) // New flow for search query
    private var currentPagingSource: ItemPagingSource? = null

    private val pager = Pager(PagingConfig(pageSize = 10)) {
        ItemPagingSource(itemDao, itemSearchFlow.value).also { currentPagingSource = it }
    }

    val lazyPagingItems = pager.flow
        .distinctUntilChanged()
        .cachedIn(viewModelScope)

    private val _itemState = MutableStateFlow(AddOrUpdateState())
    val itemStateFlow: StateFlow<AddOrUpdateState> get() = _itemState.asStateFlow()

    // Create function to insert data into the database
    fun insertItem(title: String, description: String? = null) {
        println("Inserting item")
        _itemState.value = itemStateFlow.value.copy(isLoading = true)
        // use IO dispatcher to run insert function in a background thread
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Use the current time stamp
                val currentTime = System.currentTimeMillis()
                itemDao.insertItem(ItemEntity(title = title, date = currentTime, description = description))
                _itemState.value = itemStateFlow.value.copy(isLoading = false, error = null, isSaved = true)
                // Trigger a data refresh after inserting an item
                refreshData()
            } catch (e: Exception) {
                _itemState.value = itemStateFlow.value.copy(isLoading = false, error = e.message)
            }
        }
    }

    // Set search query
    fun setSearchQuery(query: String) {
        itemSearchFlow.value = query
    }

    private fun refreshData() {
        // Trigger a data refresh
        viewModelScope.launch {
            dataRefreshFlow.emit(Unit)
        }
        currentPagingSource?.invalidate()
        currentPagingSource = ItemPagingSource(itemDao, itemSearchFlow.value)
    }
}

