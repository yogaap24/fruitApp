package com.yogaap.fruitlist.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yogaap.fruitlist.model.FruitList
import com.yogaap.fruitlist.repository.FruitRepository
import com.yogaap.fruitlist.ui.UiState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class FavoriteViewModel (private val repository: FruitRepository) : ViewModel() {
    private val _uiState: MutableStateFlow<UiState<List<FruitList>>> =
        MutableStateFlow(UiState.Loading)
    val favoriteFruits: Flow<List<FruitList>> = repository.getFruitsFavorite()
    val uiState: StateFlow<UiState<List<FruitList>>> get() = _uiState

    fun getFavoriteFruits() {
        viewModelScope.launch {
            repository.getFruitsFavorite()
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { favoriteFruitList ->
                    _uiState.value = UiState.Success(favoriteFruitList)
                }
        }
    }
}