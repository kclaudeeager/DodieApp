package com.dodie.dodieapp

data class AddOrUpdateState(
    val error: String?=null,
    val isLoading: Boolean=false,
    val isSaved: Boolean=false
)
