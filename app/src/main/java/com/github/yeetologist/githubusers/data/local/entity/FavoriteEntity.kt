package com.github.yeetologist.githubusers.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Entity(tableName = "favorite")
@Parcelize
data class FavoriteEntity (
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id")
    var id: Int,
    @field:ColumnInfo(name = "login")
    var login: String,

    @field:ColumnInfo(name = "htmlUrl")
    var htmlUrl: String,

    @field:ColumnInfo(name = "avatarUrl")
    var avatarUrl: String,
) : Parcelable