package com.github.yeetologist.githubusers.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.github.yeetologist.githubusers.data.response.FollowUserResponseItem
import com.github.yeetologist.githubusers.data.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FollowViewModel : ViewModel() {

    private val _listUsers = MutableLiveData<List<FollowUserResponseItem>?>()
    val listUsers: LiveData<List<FollowUserResponseItem>?> = _listUsers
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "FollowingViewModel"
    }

    fun findFollowing(login: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowing(login)
        client.enqueue(object : Callback<List<FollowUserResponseItem>>{
            override fun onResponse(
                call: Call<List<FollowUserResponseItem>>,
                response: Response<List<FollowUserResponseItem>>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful){
                    if (responseBody != null){
                        _listUsers.value = responseBody
                    }
                }
            }

            override fun onFailure(call: Call<List<FollowUserResponseItem>>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }

    fun findFollowers(login: String){
        _isLoading.value = true
        val client = ApiConfig.getApiService().getFollowers(login)
        client.enqueue(object : Callback<List<FollowUserResponseItem>>{
            override fun onResponse(
                call: Call<List<FollowUserResponseItem>>,
                response: Response<List<FollowUserResponseItem>>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful){
                    if (responseBody != null){
                        _listUsers.value = responseBody
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