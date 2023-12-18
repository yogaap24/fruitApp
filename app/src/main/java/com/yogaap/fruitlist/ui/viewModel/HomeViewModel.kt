package com.yogaap.fruitlist.ui.viewModel

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.yogaap.fruitlist.model.FruitList
import com.yogaap.fruitlist.repository.FruitRepository
import com.yogaap.fruitlist.ui.UiState
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class HomeViewModel(private val repository: FruitRepository) : ViewModel() {
    private val _uiState = MutableStateFlow<UiState<List<FruitList>>>(UiState.Loading)
    val uiState: StateFlow<UiState<List<FruitList>>> = _uiState

    private val _query = mutableStateOf("")
    val query: State<String> get() = _query

    fun getFruits() {
        viewModelScope.launch {
            repository.getAllFruits()
                .catch { exception ->
                    _uiState.value = UiState.Error(exception.message.toString())
                }
                .collect { listFruit ->
                    _uiState.value = UiState.Success(listFruit)
                }
        }
    }

    fun search(newQuery: String) {
        _query.value = newQuery
        viewModelScope.launch {
            repository.searchFruits(_query.value)
                .catch {
                    _uiState.value = UiState.Error(it.message.toString())
                }
                .collect { listFruit ->
                    _uiState.value = UiState.Success(listFruit)
                }
        }
    }
}