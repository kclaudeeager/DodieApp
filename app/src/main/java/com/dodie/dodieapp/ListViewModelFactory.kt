package com.dodie.dodieapp

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class ListViewModelFactory(private val application: Application) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ListViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ListViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}