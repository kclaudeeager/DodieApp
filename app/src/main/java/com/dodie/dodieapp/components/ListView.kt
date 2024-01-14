package com.dodie.dodieapp.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.paging.compose.LazyPagingItems
import com.dodie.dodieapp.sqlite.ItemEntity


@Composable
fun GetListView(
    modifier: Modifier = Modifier,
    lazyPagingItems: LazyPagingItems<ItemEntity>,
    onDelete: (ItemEntity) -> Unit,
) {

    LazyColumn(
        modifier = modifier
    ) {
        items(lazyPagingItems.itemCount) { index ->
            val item = lazyPagingItems[index]
            if (item != null) {
                Box(modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(if (index % 2 == 0) MaterialTheme.colorScheme.inversePrimary else MaterialTheme.colorScheme.tertiaryContainer)
                ){
                   Row(
                          modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween
                   ) {
                       Text(
                           text = item.title,
                           modifier = Modifier
                               .clickable {
                                   println("Clicked on item: ${item.title}")
                               }

                       )
                       // Delete button
                          Button(
                              onClick = { onDelete(item) },
                              modifier = Modifier
                                  .padding(start = 8.dp)


                          ) {
                              Text(text = "Delete")
                          }

                   }


                }

            }
        }
    }
}