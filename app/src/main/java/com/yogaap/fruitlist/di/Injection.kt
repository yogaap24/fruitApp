package com.yogaap.fruitlist.di

import com.yogaap.fruitlist.repository.FruitRepository

object Injection {
    fun provideRepository(): FruitRepository {
        return FruitRepository.getInstance()
    }
}