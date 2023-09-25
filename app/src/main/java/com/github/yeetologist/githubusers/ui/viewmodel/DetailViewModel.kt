package com.github.yeetologist.githubusers.ui.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.yeetologist.githubusers.data.FavoriteRepository
import com.github.yeetologist.githubusers.data.local.entity.FavoriteEntity
import com.github.yeetologist.githubusers.data.remote.response.DetailUserResponse
import com.github.yeetologist.githubusers.data.remote.retrofit.ApiConfig
import com.github.yeetologist.githubusers.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(app: Application) : ViewModel() {

    private val favoriteRepository: FavoriteRepository = FavoriteRepository(app)
    private val _detail = MutableLiveData<Event<DetailUserResponse>>()
    val detail: LiveData<Event<DetailUserResponse>> = _detail
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun insert(favEntity: FavoriteEntity) {
        favoriteRepository.insert(favEntity)
    }

    fun delete(favEntity: FavoriteEntity) {
        favoriteRepository.delete(favEntity)
    }

    fun getFavoriteById(id: Int): LiveData<List<FavoriteEntity>> {
        return favoriteRepository.getUserFavoriteById(id)
    }

    fun findDetailUser(login: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getDetail(login)
        client.enqueue(object : Callback<DetailUserResponse>{
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>
            ) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val responseBody = response.body()
                    if (responseBody != null) _detail.value = Event(responseBody)
                }
                else {
                    Log.e("MainActivity", "onFailure: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    companion object{
        private const val TAG = "DetailViewModel"
    }
}