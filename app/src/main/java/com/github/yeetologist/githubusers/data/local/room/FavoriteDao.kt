package com.github.yeetologist.githubusers.data.local.room

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.github.yeetologist.githubusers.data.local.entity.FavoriteEntity

@Dao
interface FavoriteDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    fun insert(fav: FavoriteEntity)

    @Update
    fun update(fav: FavoriteEntity)

    @Delete
    fun delete(fav: FavoriteEntity)

    @Query("SELECT * from favorite")
    fun getAllFavorite(): LiveData<List<FavoriteEntity>>

    @Query("SELECT * from favorite WHERE id = :id")
    fun getUserFavoriteById(id: Int): LiveData<List<FavoriteEntity>>
}