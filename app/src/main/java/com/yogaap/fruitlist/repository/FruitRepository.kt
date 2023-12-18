package com.yogaap.fruitlist.repository

import com.yogaap.fruitlist.model.FruitData
import com.yogaap.fruitlist.model.FruitList
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOf

class FruitRepository private constructor() {
    private val fruitItem = mutableListOf<FruitList>()
    private val favoriteFruits = mutableListOf<String>()
    private val listFruit = FruitData.fruits.map { FruitList(it, 0) }

    companion object {
        @Volatile
        private var instance: FruitRepository? = null

        fun getInstance(): FruitRepository =
            instance ?: synchronized(this) {
                instance ?: FruitRepository().also { instance = it }
            }
    }

    fun getAllFruits(): Flow<List<FruitList>> = flow {
        emit(listFruit)
    }

    fun getFruitById(fruitId: String): FruitList {
        return listFruit.first { it.fruit.id == fruitId }
    }

    fun searchFruits(query: String): Flow<List<FruitList>>{
        return flowOf(listFruit.filter{
            it.fruit.name.contains(query, ignoreCase = true)
        })
    }

    fun getFruitsFavorite(): Flow<List<FruitList>> {
        return flow {
            val favoriteFruitsList = fruitItem.filter { it.fruit.id in favoriteFruits }
            emit(favoriteFruitsList)
        }
    }

    fun addToFavorites(fruitId: String) {
        if (!favoriteFruits.contains(fruitId)) {
            favoriteFruits.add(fruitId)

            fruitItem.add(listFruit.first { it.fruit.id == fruitId })
        }
    }

    fun isFavorite(fruitId: String): Boolean {
        return favoriteFruits.contains(fruitId)
    }

    fun removeFromFavorites(fruitId: String) {
        favoriteFruits.remove(fruitId)
    }
}