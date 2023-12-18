package com.yogaap.fruitlist.ui.view

import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.AsyncImage
import com.yogaap.fruitlist.di.Injection
import com.yogaap.fruitlist.model.FruitList
import com.yogaap.fruitlist.ui.SearchBar
import com.yogaap.fruitlist.ui.UiState
import com.yogaap.fruitlist.ui.ViewModelFactory
import com.yogaap.fruitlist.ui.theme.FruitTheme
import com.yogaap.fruitlist.ui.viewModel.HomeViewModel

@Composable
fun FruitListItem(
    name: String,
    photoUrl: String,
    modifier: Modifier = Modifier
) {
    Card(
        shape = MaterialTheme.shapes.medium,
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer,
        ),
    ) {
        Column {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
            ) {
                AsyncImage(
                    model = photoUrl,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                )
            }
            Text(
                text = name,
                fontWeight = FontWeight.Medium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun FruitListItemPreview() {
    FruitTheme {
        FruitListItem(
            name = "Dota 2",
            photoUrl = "https://cdn.cloudflare.steamstatic.com/apps/dota2/images/dota2_social.jpg",
        )
    }
}

@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateToDetail: (String) -> Unit,
) {
    val query by viewModel.query
    Column {
        SearchBar(
            query = query,
            onQueryChange = viewModel::search
        )
        viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
            when (uiState) {
                is UiState.Loading -> {
                    viewModel.getFruits()
                }

                is UiState.Success -> {
                    HomeContent(
                        listFruit = uiState.data,
                        modifier = modifier,
                        navigateToDetail = navigateToDetail,
                    )
                }

                is UiState.Error -> {}
            }
        }
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun HomeContent(
    listFruit: List<FruitList>,
    modifier: Modifier = Modifier,
    navigateToDetail: (String) -> Unit,
) {
    Box(modifier = modifier) {
        LazyColumn {
            items(listFruit, key = { it.fruit.id }) { list ->
                FruitListItem(
                    name = list.fruit.name,
                    photoUrl = list.fruit.photoUrl,
                    modifier = Modifier
                        .clickable {
                            navigateToDetail(list.fruit.id)
                        }
                        .animateItemPlacement(tween(durationMillis = 100))
                )
            }
        }
    }
}