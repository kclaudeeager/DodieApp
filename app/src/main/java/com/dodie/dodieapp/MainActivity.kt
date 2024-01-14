package com.dodie.dodieapp

import android.content.Intent
import android.os.Bundle
import android.widget.ListView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import androidx.core.content.ContextCompat.startActivity
import com.dodie.dodieapp.ui.theme.DodieAppTheme
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import kotlinx.coroutines.launch

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            DodieAppTheme {
                DodieAppContent()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPagerApi::class)
@Composable
fun DodieAppContent() {
    val courtineLauncher = rememberCoroutineScope()
    Scaffold(
        topBar = {
            TopAppBar(
                navigationIcon = {
                    Row {
                        IconButton(onClick = { /*TODO*/ }) {
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
        }
    ) {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            val cousins = listOf(
                Cousin("Cousin 1", R.drawable.img_1, 3),
                Cousin("Cousin 2", R.drawable.img_2, 4),
                Cousin("Cousin 3", R.drawable.img_3, 5)
            )
            val pagerState = rememberPagerState(initialPage = 0)

            HorizontalPager(
                state = pagerState,
                count = cousins.size,
                modifier = Modifier.padding(16.dp)
            ) { page ->
                val cousin = cousins[page]

                DodieItem(cousin = cousin, isLast = page == cousins.size - 1, isFirst = page == 0,
                    onNextClick = {
                        courtineLauncher.launch {
                            pagerState.animateScrollToPage(page + 1)
                        }
                    },
                    onPreviousClick = {
                        courtineLauncher.launch {
                        pagerState.animateScrollToPage(page - 1)
                    }
                    })
            }
        }
    }
}

data class Cousin(
    val name: String,
    val image: Int,
    val rating: Int,
    val address: String = "132 The Embarcadero, San Francisco,\n" +
            "CA 94105",
    val hours: String = "Lunch Mon.-Fri., Dinner nightly"
)

@Composable
fun DodieItem(cousin: Cousin, isLast: Boolean = false, isFirst: Boolean = false,onNextClick: () -> Unit = {},onPreviousClick: () -> Unit = {}) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Image(
            painter = painterResource(id = cousin.image),
            contentDescription = cousin.name,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp) // Set the desired height
                .border(1.5.dp, Color.Gray)
                .clickable { /* Handle image click if needed */ },
            contentScale = ContentScale.Crop,
        )

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            if (!isFirst)
                IconButton(onClick = {
                    onPreviousClick()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_arrow_back_ios_new_24),
                        contentDescription = "Previous"
                    )
                }

            Column {
                Text(
                    text = "Cousin",
                    style = MaterialTheme.typography.bodySmall
                )
                Text(
                    text = cousin.name,
                    style = MaterialTheme.typography.bodySmall.copy(
                        fontWeight = FontWeight.Bold
                    )
                )
            }

            Spacer(modifier = Modifier.width(8.dp))

            RatingBar(rating = cousin.rating)

            Spacer(modifier = Modifier.width(8.dp))

            if (!isLast)
                IconButton(onClick = {
                    onNextClick()
                }) {
                    Icon(
                        painter = painterResource(id = R.drawable.rrow_forward),
                        contentDescription = "Next"
                    )
                }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Divider(modifier = Modifier.fillMaxWidth(), color = Color.Gray, thickness = 1.dp)

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Address:",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = cousin.address,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(8.dp))


            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Hours:",
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.padding(end = 8.dp)
                )
                Text(
                    text = cousin.hours,
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier.weight(1f)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { /* Handle Map button click */ },
                    modifier = Modifier
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.Gray
                    ),
                    shape = RectangleShape
                ) {
                    Text(
                        text = "Map",
                        color = Color.Black,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
                Button(
                    onClick = { /* Handle Reserve Now button click */ },
                    modifier = Modifier
                        .weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        //use sky blue color
                        containerColor = Color(0xFF00BFFF)
                    ),
                    shape = RectangleShape
                ) {
                    Text(
                        text = "Reserve Now",
                        color = Color.White,
                        style = MaterialTheme.typography.bodySmall.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                }
            }
            Spacer(modifier = Modifier.height(32.dp))
            Divider(modifier = Modifier.fillMaxWidth(), color = Color(0xFF00BFFF), thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))

// Add a footer showing plust button,number of rattings, and share button
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
            Button(onClick = {
                val intent = Intent(context, ListViweActivity::class.java)
                context.startActivity(intent)
            }) {
                Text(text = "Go to list")
            }
                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(id = R.drawable.add),
                        contentDescription = "Next"
                    )
                }
                Box(
                    modifier = Modifier
                        .alpha(0.9f)
                        .padding(1.dp)
                        .size(80.dp)
                ) {

                    Row(
                        modifier = Modifier
                            .background(Color.Gray)
                            .padding(1.dp)


                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.star_rate),
                            contentDescription = "Rating",
                            tint = Color.White
                        )
                        Icon(
                            painter = painterResource(id = R.drawable.star_rate),
                            contentDescription = "Rating",
                            tint = Color.White
                        )
                    }

                    Box(
                        modifier = Modifier
                            .align(Alignment.TopEnd)
                            .offset(x = (3).dp, y = (-10).dp) // Offset the box
                            .size(25.dp)
                            .background(Color.Gray)
                            .border(
                                width = 1.dp,
                                color = Color.White,
                                shape = RectangleShape
                            )
                    ) {
                        Text(
                            text = "${cousin.rating*5}",
                            style = MaterialTheme.typography.bodySmall.copy(
                                fontWeight = FontWeight.Bold
                            ),
                            color = Color.White,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                }

                IconButton(onClick = {}) {
                    Icon(
                        painter = painterResource(id = R.drawable.share),
                        contentDescription = "Share"
                    )
                }
            }
        }
    }
}


@Composable
fun RatingBar(rating: Int) {
    Column {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {

            repeat(rating) {
                // Rated star with red background
                Box(
                    modifier = Modifier
                        .background(color = Color.Red)
                        .alpha(0.9f)
                        .padding(1.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.star_rate),
                        contentDescription = "Rating",
                        tint = Color.White
                    )
                }

            }
            repeat(5 - rating) {
                // Unrated star without background
                Box(
                    modifier = Modifier
                        .background(color = Color.Gray)
                        .alpha(0.9f)
                        .padding(1.dp)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.star_rate),
                        contentDescription = "Rating",
                        tint = Color.White
                    )
                }

            }
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = "${rating * 5} reviews",
            style = MaterialTheme.typography.bodySmall.copy(
                fontWeight = FontWeight.Bold
            ),
            modifier = Modifier.padding(start = 16.dp, end = 16.dp)
        )
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    DodieAppTheme {
        DodieAppContent()
    }
}
