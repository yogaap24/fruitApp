package com.yogaap.fruitlist.ui.viewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yogaap.fruitlist.model.FruitList
import com.yogaap.fruitlist.repository.FruitRepository
import com.yogaap.fruitlist.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailViewModel (private val repository: FruitRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<FruitList>>(UiState.Loading)
    val uiState: StateFlow<UiState<FruitList>> = _uiState

    fun getFruitById(fruitId: String) {
        viewModelScope.launch {
            _uiState.value = UiState.Loading
            _uiState.value = UiState.Success(repository.getFruitById(fruitId))
        }
    }

    fun addToFavorites(fruitId: String) {
        viewModelScope.launch {
            repository.addToFavorites(fruitId)
        }
    }

    fun removeFromFavorite(fruitId: String) {
        viewModelScope.launch {
            repository.removeFromFavorites(fruitId)
        }
    }

    fun checkFavorite(fruitId: String, onResult: (Boolean) -> Unit) {
        viewModelScope.launch {
            val isFavorite = repository.isFavorite(fruitId)
            onResult(isFavorite)
        }
    }
}