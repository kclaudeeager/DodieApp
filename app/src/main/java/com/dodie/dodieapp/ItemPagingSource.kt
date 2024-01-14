package com.dodie.dodieapp

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.dodie.dodieapp.sqlite.ItemDao
import com.dodie.dodieapp.sqlite.ItemEntity
import kotlinx.coroutines.flow.firstOrNull

class ItemPagingSource(
    private val itemDao: ItemDao,
    private val query: String? // Accept a search query
) : PagingSource<Int, ItemEntity>() {
    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ItemEntity> {
        try {
            val position = params.key ?: 1 // Start from page 1
            val items = if (query.isNullOrBlank()) {
                itemDao.loadItems((position - 1) * params.loadSize, params.loadSize).firstOrNull()
            } else {
                itemDao.searchItems("%${query.trim()}%", (position - 1) * params.loadSize, params.loadSize).firstOrNull()
            }

            return if (items != null) {
                LoadResult.Page(
                    data = items,
                    prevKey = if (position == 1) null else position - 1,
                    nextKey = if (items.isEmpty()) null else position + 1
                )
            } else {
                LoadResult.Error(Exception("Failed to load data"))
            }
        } catch (e: Exception) {
            return LoadResult.Error(e)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, ItemEntity>): Int? {
        return state.anchorPosition
    }
}

