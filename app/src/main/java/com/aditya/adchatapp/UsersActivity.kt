package com.aditya.adchatapp

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aditya.adchatapp.adapter.ChatAdapter
import com.aditya.adchatapp.models.User
import com.aditya.adchatapp.adapter.UserAdapter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import io.socket.client.IO
import io.socket.client.Socket
import okhttp3.*
import org.json.JSONArray
import java.io.IOException
import java.net.URISyntaxException

class UsersActivity : AppCompatActivity() {

    private lateinit var socket: Socket
    private lateinit var userList: MutableList<User>
    private lateinit var userAdapter: UserAdapter
    private lateinit var senderNickname: String
    private lateinit var recyclerView: RecyclerView
    private lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_users)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)
        userList = mutableListOf()
        userAdapter = UserAdapter(userList)
        recyclerView.adapter = userAdapter

        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)
        senderNickname = sharedPreferences.getString("senderName", "").toString()

        // Get the entered nickname from the previous activity (e.g., from an Intent)

        connectToSocketIO()
    }

    private fun connectToSocketIO() {
        try {



            socket = IO.socket("http://192.168.29.40:3000") // Replace with your server URL
            socket.connect()

            // Send the entered nickname to the server when the user joins the chat

            socket.emit("join", senderNickname)

            // Listen for real-time updates
            listenForRealTimeUpdates()

            // Fetch initial user list from the server
            fetchUserListFromBackend()

        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }
    }


    private fun listenForRealTimeUpdates() {
        // Listen for user_joined event from the server
//        socket.on("userjoinedthechat") { args ->
//            runOnUiThread {
//                val userID = args[0].toString()
//                val userNickname = args[1].toString()
//                val existingUser = userList.find { it.username == userNickname }
//                if (existingUser != null) {
//                    existingUser.isOnline = true
//                } else {
//                    userList.add(User(userID,userNickname, isOnline = false))
//                }
//                userAdapter.notifyDataSetChanged()
//            }
//        }

        // Listen for user_disconnect event from the server
//        socket.on("userdisconnect") { args ->
//            runOnUiThread {
//                val username = args[0].toString()
//                val user = userList.find { it.username == username }
//                user?.isOnline = false
//                userAdapter.notifyDataSetChanged()
//            }
//        }

        // Listen for userList event from the server
        socket.on("userList") { args ->
            runOnUiThread {
                val userListJsonArray = args[0] as JSONArray
                val onlineUsers = mutableListOf<String>()

                for (i in 0 until userListJsonArray.length()) {
                    val username = userListJsonArray.getString(i)
                    onlineUsers.add(username)
                }

                // Update your user list or UI here
                userList.forEach { user ->
                    user.isOnline = onlineUsers.contains(user.username)
                }

                userAdapter.notifyDataSetChanged()
            }
        }



    }


    override fun onStop() {
        super.onStop()
        // Disconnect the Socket.IO when the activity is stopped to avoid memory leaks
        socket.disconnect()
    }


//    override fun onBackPressed() {
//        // Disconnect the Socket.IO when the back button is pressed
//        socket.emit("userdisconnected", enteredNickname)
//        socket.disconnect()
//        super.onBackPressed()
//    }


    override fun onDestroy() {
        super.onDestroy()
        socket.emit("userdisconnected",senderNickname)
        socket.disconnect()

    }


    private fun fetchUserListFromBackend() {
        val client = OkHttpClient()
        val request = Request.Builder()
            .url("http://192.168.29.40:3000/users") // Replace with your backend API URL
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure, e.g., show an error message or retry logic
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    // Handle non-successful response, e.g., show an error message
                    return
                }

                val responseBody = response.body?.string()


                if (responseBody.isNullOrEmpty()) {
                    // Handle empty response body
                    Log.e("GET", "Response body is null or empty.")
                    return
                }

                try {



                    val gson = Gson()
                    val type = object : TypeToken<List<User>>() {}.type
                    val users = gson.fromJson<List<User>>(responseBody, type)

                    if (users.isNotEmpty()) {
                        // Filter out the signed-in user from the list
                        val filteredUsers = users.filter { it.username != senderNickname  }

                        // Add the filtered usernames to the userList
                        userList.addAll(filteredUsers)

                        Log.d("data","$userList")


                        // Update the RecyclerView by notifying the adapter
                        runOnUiThread {
                            userAdapter.notifyDataSetChanged()
                        }
                    } else {
                        // Handle empty users list
                        Log.e("GET", "User list is empty.")
                    }
                } catch (e: Exception) {
                    // Handle parsing error, e.g., show an error message
                    Log.e("GET", "Error parsing JSON: ${e.message}")
                }
            }
        })
    }


}
