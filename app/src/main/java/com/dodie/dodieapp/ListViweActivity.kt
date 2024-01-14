package com.dodie.dodieapp

import android.annotation.SuppressLint
import android.app.Application
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.compose.collectAsLazyPagingItems
import com.dodie.dodieapp.components.AddItem
import com.dodie.dodieapp.components.GetListView
import com.dodie.dodieapp.ui.theme.DodieAppTheme
import kotlinx.coroutines.launch

class ListViweActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    @OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DodieAppTheme {

                val context = LocalContext.current
                val application = context.applicationContext as Application
                val listViewModel: ListViewModel =
                    viewModel(factory = ListViewModelFactory(application))
                val lazyPagingItems = listViewModel.lazyPagingItems.collectAsLazyPagingItems()
                val onBackPressedDispatcher = this.onBackPressedDispatcher
                val itemStateFlow = listViewModel.itemStateFlow.collectAsStateWithLifecycle()
                val snackBarHostState = remember { SnackbarHostState() }
                val isShowBottomSheet = remember { mutableStateOf(false) }
                val sheetState = rememberModalBottomSheetState()
                val coroutineScope = rememberCoroutineScope()
                val softwareKeyboardController = LocalSoftwareKeyboardController.current
                var searchQuery by remember { mutableStateOf("") }

                LaunchedEffect(itemStateFlow.value) {
                    if (itemStateFlow.value.isSaved) {
                        snackBarHostState.showSnackbar("New Item saved")
                    }
                    if (itemStateFlow.value.error != null) {
                        snackBarHostState.showSnackbar(itemStateFlow.value.error!!)
                    }
                }
                LaunchedEffect(lazyPagingItems) {
                    println("Refreshing data")
                    lazyPagingItems.refresh()
                }

                Scaffold(
                    snackbarHost = {
                        SnackbarHost(hostState = snackBarHostState)
                    },
                    topBar = {
                        TopAppBar(
                            navigationIcon = {
                                Row {
                                    IconButton(onClick = {
                                        onBackPressedDispatcher.onBackPressed()
                                    }) {
                                        Icon(
                                            painter = painterResource(id = R.drawable.baseline_arrow_back_ios_new_24),
                                            contentDescription = "Back"
                                        )
                                    }
                                    Image(
                                        painter = painterResource(id = R.drawable.img_6),
                                        contentDescription = "Logo",
                                        modifier = Modifier
                                            .size(40.dp)
                                            .padding(8.dp),
                                        contentScale = ContentScale.Crop,
                                    )
                                }

                            },
                            title = {
                                Text(text = "Chaya Brasserie")
                            },
                        )
                    },
                    floatingActionButton = {
                        FloatingActionButton(
                            onClick = {
                                isShowBottomSheet.value = !isShowBottomSheet.value
                            },
                            modifier = Modifier
                                .padding(16.dp)
                                .zIndex(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "Add item"
                            )
                        }
                    }
                ) {
                    Surface(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(it)
                    ) {
                        Column(
                            // Center the content
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
                        ) {
                            // Search bar
                            OutlinedTextField(
                                value = searchQuery,
                                onValueChange = { newQuery ->
                                    searchQuery = newQuery
                                    // Update the search query in the ViewModel

                                    listViewModel.setSearchQuery(newQuery)
                                    lazyPagingItems.refresh()

                                },
                                placeholder = { Text("Search") },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(8.dp),
                                keyboardOptions = KeyboardOptions.Default.copy(
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        // Close the keyboard when the Done action is triggered
                                        softwareKeyboardController?.hide()
                                    }
                                )
                            )
                            Text(
                                text = "Items list",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(16.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))


                            GetListView(
                                modifier = Modifier
                                    .fillMaxSize(),
                                lazyPagingItems = lazyPagingItems,
                                onDelete = { item ->
                                    println("Delete item ${item.title}")
                                }
                            )
                        }
                    }
                    if (isShowBottomSheet.value) {
                        ModalBottomSheet(
                            sheetState = sheetState,
                            onDismissRequest = {
                                coroutineScope.launch {
                                    sheetState.hide()
                                    isShowBottomSheet.value = false
                                }
                            },
                        ) {
                            // UI for adding new item
                            AddItem(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(16.dp)
                            ) { title, description ->
                                // Add item to the database
                                listViewModel.insertItem(title, description)
                                coroutineScope.launch {
                                    sheetState.hide()
                                    isShowBottomSheet.value = false
                                }
                            }
                        }
                    }
                }
            }
        }
    }


}