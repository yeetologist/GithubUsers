package com.github.yeetologist.githubusers.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import com.github.yeetologist.githubusers.data.SettingPreferences
import com.github.yeetologist.githubusers.data.remote.response.ItemsItem
import com.github.yeetologist.githubusers.data.remote.response.SearchResponse
import com.github.yeetologist.githubusers.data.remote.retrofit.ApiConfig
import com.github.yeetologist.githubusers.util.Event
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(private val pref: SettingPreferences) : ViewModel() {

    private val _resultCount = MutableLiveData<Event<Int>>()
    val resultCount: LiveData<Event<Int>> = _resultCount
    private val _listUsers = MutableLiveData<Event<List<ItemsItem>>>()
    val listUsers: LiveData<Event<List<ItemsItem>>> = _listUsers
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

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
    fun getThemeSettings(): LiveData<Boolean> {
        return pref.getThemeSetting().asLiveData()
    }

    companion object{
        private const val TAG = "MainViewModel"
    }
}