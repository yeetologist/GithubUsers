package com.github.yeetologist.githubusers.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.yeetologist.githubusers.data.response.ItemsItem
import com.github.yeetologist.githubusers.data.response.SearchResponse
import com.github.yeetologist.githubusers.data.retrofit.ApiConfig
import com.github.yeetologist.githubusers.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _resultCount = MutableLiveData<Event<Int>>()
    val resultCount: LiveData<Event<Int>> = _resultCount
    private val _listUsers = MutableLiveData<Event<List<ItemsItem>>>()
    val listUsers: LiveData<Event<List<ItemsItem>>> = _listUsers
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "MainViewModel"
    }

    fun findUser(query: String, page: Int = 1){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getSearch(query, page)
        client.enqueue(object : Callback<SearchResponse> {
            override fun onResponse(
                call: Call<SearchResponse>,
                response: Response<SearchResponse>
            ) {
                if (response.isSuccessful) {
                    _isLoading.value = false
                    val responseBody = response.body()
                    if (responseBody != null) {
                        _listUsers.value = Event(responseBody.items)
                        _resultCount.value = Event(responseBody.totalCount)
                    } else {
                        Log.e(TAG, "onFailure: ${response.message()}")
                    }
                }
            }

            override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}