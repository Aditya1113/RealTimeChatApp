package com.aditya.adchatapp.models

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import okhttp3.Call
import okhttp3.Callback
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import java.io.IOException

data class User(var userId:String, val username: String, var isOnline: Boolean = true)





