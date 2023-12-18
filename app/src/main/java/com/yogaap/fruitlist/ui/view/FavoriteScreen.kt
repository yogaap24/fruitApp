package com.yogaap.fruitlist.ui.view

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.yogaap.fruitlist.di.Injection
import com.yogaap.fruitlist.model.FruitList
import com.yogaap.fruitlist.ui.UiState
import com.yogaap.fruitlist.ui.ViewModelFactory
import com.yogaap.fruitlist.ui.viewModel.FavoriteViewModel

@Composable
fun FavoriteScreen(
    modifier: Modifier = Modifier,
    viewModel: FavoriteViewModel = viewModel(
        factory = ViewModelFactory(Injection.provideRepository())
    ),
    navigateBack: () -> Unit,
    navigateToDetail: (String) -> Unit
) {
    val favoriteFruits by viewModel.favoriteFruits.collectAsState(emptyList())

    viewModel.uiState.collectAsState(initial = UiState.Loading).value.let { uiState ->
        when (uiState) {
            is UiState.Loading -> {
                viewModel.getFavoriteFruits()
            }

            is UiState.Success -> {
                FavoriteContent(
                    favoriteFruits = favoriteFruits,
                    modifier = modifier,
                    onBackClick = navigateBack,
                    navigateToDetail = navigateToDetail
                )
            }

            is UiState.Error -> {}
        }
    }
}

@Composable
fun FavoriteContent(
    favoriteFruits: List<FruitList>,
    onBackClick: () -> Unit,
    modifier: Modifier = Modifier,
    navigateToDetail: (String) -> Unit
) {
    Column(
        modifier = modifier.padding(8.dp)
    ) {
        Box {
            Row(
                modifier = modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    tint = Color.Black,
                    contentDescription = "Kembali",
                    modifier = Modifier
                        .padding(6.dp)
                        .clickable { onBackClick() }
                )
                Text(
                    text = "Favorite",
                    style = TextStyle(fontWeight = FontWeight.Bold, fontSize = 16.sp),
                    modifier = modifier
                        .padding(start = 9.dp)
                        .weight(1f)
                )
            }
        }
        Column {
            if (favoriteFruits.isEmpty()) {
                Text(
                    text = "Buah Favorite kamu masih Kosong",
                    modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                    color = Color.LightGray,
                    textAlign = TextAlign.Justify
                )
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    contentPadding = PaddingValues(17.dp),
                    horizontalArrangement = Arrangement.spacedBy(17.dp),
                    verticalArrangement = Arrangement.spacedBy(17.dp),
                ) {
                    items(favoriteFruits) { data ->
                        FruitListItem(
                            name = data.fruit.name,
                            photoUrl = data.fruit.photoUrl,
                            modifier = Modifier.clickable {
                                navigateToDetail(data.fruit.id)
                            }
                        )
                    }
                }
            }
        }
    }
}