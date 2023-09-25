package com.github.yeetologist.githubusers.ui.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.github.yeetologist.githubusers.data.FavoriteRepository
import com.github.yeetologist.githubusers.data.local.entity.FavoriteEntity

class FavoriteViewModel(application : Application) : ViewModel()  {
    private val favRepository : FavoriteRepository = FavoriteRepository(application)
    fun getAllFavorites() : LiveData<List<FavoriteEntity>> = favRepository.getAllFavorite()
}