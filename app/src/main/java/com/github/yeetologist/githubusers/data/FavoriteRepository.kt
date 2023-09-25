package com.github.yeetologist.githubusers.data

import android.app.Application
import androidx.lifecycle.LiveData
import com.github.yeetologist.githubusers.data.local.entity.FavoriteEntity
import com.github.yeetologist.githubusers.data.local.room.FavoriteDao
import com.github.yeetologist.githubusers.data.local.room.FavoriteDatabase
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavoriteRepository(application: Application) {
    private val favoriteDao: FavoriteDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()
    init {
        val db = FavoriteDatabase.getDatabase(application)
        favoriteDao = db.favoriteDao()
    }
    fun getAllFavorite(): LiveData<List<FavoriteEntity>> = favoriteDao.getAllFavorite()
    fun insert(favoriteEntity: FavoriteEntity) {
        executorService.execute { favoriteDao.insert(favoriteEntity) }
    }
    fun delete(favoriteEntity: FavoriteEntity) {
        executorService.execute { favoriteDao.delete(favoriteEntity) }
    }
    fun update(favoriteEntity: FavoriteEntity) {
        executorService.execute { favoriteDao.update(favoriteEntity) }
    }

    fun getUserFavoriteById(id: Int): LiveData<List<FavoriteEntity>> =
        favoriteDao.getUserFavoriteById(id)

}