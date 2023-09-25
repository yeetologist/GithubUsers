package com.github.yeetologist.githubusers.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.yeetologist.githubusers.data.response.FollowUserResponseItem
import com.github.yeetologist.githubusers.data.remote.retrofit.ApiConfig
import com.github.yeetologist.githubusers.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {

    private val _listFollowers = MutableLiveData<Event<List<FollowUserResponseItem>>>()
    val listFollowers: LiveData<Event<List<FollowUserResponseItem>>> = _listFollowers
    private val _listFollowing = MutableLiveData<Event<List<FollowUserResponseItem>>>()
    val listFollowing: LiveData<Event<List<FollowUserResponseItem>>> = _listFollowing
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading
    var isFunctionExecuted = false

    companion object{
        private const val TAG = "FollowingViewModel"
    }

    fun findFollowing(login: String, page: Int = 1){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(login, page)
        client.enqueue(object : Callback<List<FollowUserResponseItem>>{
            override fun onResponse(
                call: Call<List<FollowUserResponseItem>>,
                response: Response<List<FollowUserResponseItem>>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful){
                    val currentList = _listFollowing.value?.peekContent()?.toMutableList() ?: mutableListOf()
                    if (responseBody != null){
                        currentList.addAll(responseBody)
                        _listFollowing.value = Event(currentList)
                    }
                }
            }

            override fun onFailure(call: Call<List<FollowUserResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun findFollowers(login: String, page: Int = 1){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(login, page)
        client.enqueue(object : Callback<List<FollowUserResponseItem>>{
            override fun onResponse(
                call: Call<List<FollowUserResponseItem>>,
                response: Response<List<FollowUserResponseItem>>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful){
                    val currentList = _listFollowers.value?.peekContent()?.toMutableList() ?: mutableListOf()
                    if (responseBody != null){
                        currentList.addAll(responseBody)
                        _listFollowers.value = Event(currentList)
                    }
                }
            }

            override fun onFailure(call: Call<List<FollowUserResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}