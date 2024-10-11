package com.example.socialapp

import android.util.Log
import android.view.LayoutInflater
import android.widget.ProgressBar
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.socialapp.apiservices.AppApi
import com.example.socialapp.apiservices.PostData
import com.example.socialapp.apiservices.UserDetails
import com.example.socialapp.fragments.UsersFragment
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Response
import retrofit2.http.PUT
import java.util.zip.Inflater

class ListViewModel : ViewModel() {
    val listData: MutableLiveData<List<UserDetails>> = MutableLiveData()
    val data: MutableLiveData<Int> = MutableLiveData()
    val postData: MutableLiveData<Response<PostData>> = MutableLiveData()
    val putData:MutableLiveData<Response<PostData>> = MutableLiveData()

    fun getDetails() {
        viewModelScope.launch(Dispatchers.IO) {
            val result = AppApi.getRetrofitInstance().getUsersDetails()
            if (result.isNotEmpty()) {
                listData.postValue(result)
            }else {
                Log.e("TAG","Error to load the data from server")
            }
        }
    }

    fun deleteUser(userId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            val result = AppApi.getRetrofitInstance().deleteUser(userId)
            if (result.isSuccessful) {
                data.postValue(result.code())
            } else {
                Log.e("TAG", "Error to get response of delete from server")
            }
        }
    }
    fun postData(post:PostData){
        viewModelScope.launch(Dispatchers.IO) {
            val response = AppApi.getRetrofitInstance().postDataToServer(post)

            if (response.isSuccessful) {
                postData.postValue(response)
            }else{
                Log.e("TAG", "Error to get response of post from server")
            }
        }
    }
    fun putData(userId: Int,put:PostData){
        viewModelScope.launch(Dispatchers.IO) {
            val response=AppApi.getRetrofitInstance().putData(userId,put)
            putData.postValue(response)
        }
    }
}