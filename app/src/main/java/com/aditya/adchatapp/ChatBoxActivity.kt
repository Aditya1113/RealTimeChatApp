//package com.aditya.adchatapp
//
//import android.os.Bundle
//
//import android.widget.Button
//import android.widget.EditText
//import android.widget.TextView
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.constraintlayout.widget.ConstraintLayout
//import androidx.recyclerview.widget.DividerItemDecoration
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.aditya.adchatapp.adapter.ChatAdapter
//import com.aditya.adchatapp.adapter.UserAdapter
//import com.aditya.adchatapp.models.Message
//import com.aditya.adchatapp.models.User
//import com.google.android.material.snackbar.Snackbar
//import io.socket.client.IO
//import io.socket.client.Socket
//import io.socket.emitter.Emitter
//import org.json.JSONException
//import org.json.JSONObject
//import java.net.URISyntaxException
//
//class ChatBoxActivity : AppCompatActivity() {
//
//    lateinit var chatLayout : ConstraintLayout
//    lateinit var socket : Socket
//    lateinit var messageRecycler: RecyclerView
//    private lateinit var userList: MutableList<User>
//    private lateinit var userAdapter: UserAdapter
//    lateinit  var messageSendBTN :Button
//    lateinit var messageTXT : EditText
//    var nickNameRcvdValue = String()
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_chat_box)
//
//        //Linking of UI Control of Chat Box Activity Layout
//        chatLayout = findViewById(R.id.chboxactivity_layout)
//        messageTXT = findViewById<EditText>(R.id.messageTXTED)
//        messageSendBTN = findViewById<Button>(R.id.messageSentBTN)
//        messageRecycler = findViewById<RecyclerView>(R.id.messageRV)
//
//        messageRecycler.layoutManager = LinearLayoutManager(this)
//        messageRecycler.addItemDecoration(DividerItemDecoration(this,LinearLayoutManager.VERTICAL))
//
//        userList = mutableListOf()
//        userAdapter = UserAdapter(userList)
//
//    }
//
//    override fun onResume() {
//        super.onResume()
//
//        //Receive Value from MainActivity NickName value
//        nickNameRcvdValue = intent.getStringExtra("username").toString()
//        Toast.makeText(this,nickNameRcvdValue,Toast.LENGTH_LONG).show()
//
//        //Socket Connection
//        try{
//            socket = IO.socket("http://192.168.29.40:3000")
//            socket.connect()
//            socket.emit("join",nickNameRcvdValue)
//
//        }catch (e: URISyntaxException){
//            e.printStackTrace()
//        }
//
//        //Display Snack Bar once any one other Joined the chat
//        socket.on("userjoinedthechat", Emitter.Listener { args->
//            val data = args[0]
//            runOnUiThread(Runnable {
//
//                val snackBar= Snackbar.make(chatLayout, data.toString(),Snackbar.LENGTH_LONG)
//                snackBar.show()
//            })
//        })
//
//        //Display Snack Bar when User disconnected
//        socket.on("userdisconnect",Emitter.Listener { args ->
//            val data = args[0]
//            runOnUiThread(Runnable {
//                val snackBar= Snackbar.make(chatLayout, data.toString(),Snackbar.LENGTH_LONG)
//                snackBar.show()
//            })
//        })
//
//        var messageListArray = arrayListOf<Message>()
//        messageSendBTN.setOnClickListener {
//            //Sending Message through Socket
//            var messageValue = messageTXT.text.toString()
//            socket.emit("messagedetection")
//
//            socket.emit("messagedetection",nickNameRcvdValue,messageValue);
//            messageTXT.text.clear()
//        }
//        socket.on("message",Emitter.Listener { args->
//            run {
//                runOnUiThread(Runnable {
//                    var data = args[0] as JSONObject
//                    try {
//                        var nickName = data.getString("senderNickname")
//                        var message = data.getString("message")
//
//                        var newMsg = Message(nickName, message)
//                        messageListArray.add(newMsg)
//
//                        var adapter = ChatAdapter(messageListArray)
//                        adapter.notifyDataSetChanged()
//                        messageRecycler.adapter = adapter
//
//
//                    } catch (e: JSONException) {
//                        e.printStackTrace()
//                    }
//                })
//            }
//        })
//
//    }
//
////    override fun onBackPressed() {
////        // Disconnect the Socket.IO when the back button is pressed
////        socket.emit("userdisconnected")
////        socket.disconnect()
////        super.onBackPressed()
////    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        socket.emit("userdisconnected",nickNameRcvdValue)
//        socket.disconnect()
//
//    }
//}


//second version
//package com.aditya.adchatapp
//import android.os.Bundle
//import android.widget.Button
//import android.widget.EditText
//import android.widget.Toast
//import androidx.appcompat.app.AppCompatActivity
//import androidx.constraintlayout.widget.ConstraintLayout
//import androidx.recyclerview.widget.DividerItemDecoration
//import androidx.recyclerview.widget.LinearLayoutManager
//import androidx.recyclerview.widget.RecyclerView
//import com.aditya.adchatapp.adapter.ChatAdapter
//import com.aditya.adchatapp.models.Message
//import com.android.volley.Request
//import com.android.volley.toolbox.JsonObjectRequest
//import com.android.volley.toolbox.Volley
//import com.google.android.material.snackbar.Snackbar
//import io.socket.client.IO
//import io.socket.client.Socket
//import io.socket.emitter.Emitter
//import org.json.JSONException
//import org.json.JSONObject
//import java.net.URISyntaxException
//
//class ChatBoxActivity : AppCompatActivity() {
//
//    private lateinit var chatLayout: ConstraintLayout
//    private lateinit var socket: Socket
//    private lateinit var messageRecycler: RecyclerView
//    private lateinit var messageListArray: ArrayList<Message>
//    private lateinit var chatAdapter: ChatAdapter
//    private lateinit var messageSendBTN: Button
//    private lateinit var messageTXT: EditText
//    private var nickNameRcvdValue: String = ""
//    private lateinit var userId : String
//
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_chat_box)
//
//
//        //Linking of UI Control of Chat Box Activity Layout
//        chatLayout = findViewById(R.id.chboxactivity_layout)
//        messageTXT = findViewById<EditText>(R.id.messageTXTED)
//        messageSendBTN = findViewById<Button>(R.id.messageSentBTN)
//        messageRecycler = findViewById<RecyclerView>(R.id.messageRV)
//
//        messageRecycler.layoutManager = LinearLayoutManager(this)
//        messageRecycler.addItemDecoration(DividerItemDecoration(this,LinearLayoutManager.VERTICAL))
//
//        messageListArray = ArrayList()
//        chatAdapter = ChatAdapter(messageListArray)
//        messageRecycler.adapter = chatAdapter
//        userId = intent.getStringExtra("FriendId").toString()
//    }
//
//    override fun onResume() {
//        super.onResume()
//
//        // Receive Value from MainActivity NickName value
//        nickNameRcvdValue = intent.getStringExtra("FriendName").toString()
//        userId = intent.getStringExtra("FriendId").toString()
//        Toast.makeText(this, nickNameRcvdValue, Toast.LENGTH_LONG).show()
//
//
//        // Socket Connection
//        try {
//            socket = IO.socket("http://192.168.29.40:3000")
//            socket.connect()
//            socket.emit("join", nickNameRcvdValue)
//
//        } catch (e: URISyntaxException) {
//            e.printStackTrace()
//        }
//
//        // Display Snack Bar once anyone else Joins the chat
//        socket.on("userjoinedthechat", Emitter.Listener { args ->
//            val data = args[0].toString()
//            runOnUiThread {
//                val snackBar = Snackbar.make(chatLayout, data, Snackbar.LENGTH_LONG)
//                snackBar.show()
//            }
//        })
//
//        // Display Snack Bar when User disconnects
//        socket.on("userdisconnect", Emitter.Listener { args ->
//            val data = args[0].toString()
//            runOnUiThread {
//                val snackBar = Snackbar.make(chatLayout, data, Snackbar.LENGTH_LONG)
//                snackBar.show()
//            }
//        })
//
//        messageSendBTN.setOnClickListener {
//            // Sending Message through Socket
//            val messageValue = messageTXT.text.toString()
////            socket.emit("messagedetection")
//            socket.emit("messagedetection", nickNameRcvdValue, messageValue,userId)
//            messageTXT.text.clear()
//        }
//
//        socket.on("message", Emitter.Listener { args ->
//            runOnUiThread {
//                val data = args[0] as JSONObject
//                try {
//                        var nickName = data.getString("senderNickname")
//                    var message = data.getString("message")
//                    var chatTime = data.getString("chatTime")
//
//                        var newMsg = Message(nickName, message, chatTime)
//                        messageListArray.add(newMsg)
//
//                        var adapter = ChatAdapter(messageListArray)
//                        adapter.notifyDataSetChanged()
//                        messageRecycler.adapter = adapter
//
//
//                    } catch (e: JSONException) {
//                    e.printStackTrace()
//                }
//            }
//        })
//
//        // Fetch chat history from the database for the logged-in user
//        fetchChatHistoryFromDatabase(userId)
//    }
//
//    override fun onDestroy() {
//        super.onDestroy()
//        socket.emit("userdisconnected", nickNameRcvdValue)
//        socket.disconnect()
//    }
//
//
//
//    private fun fetchChatHistoryFromDatabase(userId: String) {
//        val url = "http://192.168.29.40:3000/chatHistory/$userId"
//
//        // Create a new JsonObjectRequest using GET method
//        val request = JsonObjectRequest(
//            Request.Method.GET, url, null,
//            { response ->
//                // Parse the response JSON and extract chat history
//                try {
//                    val messagesArray = response.getJSONArray("messages")
//                    val messageListArray = ArrayList<Message>()
//
//                    for (i in 0 until messagesArray.length()) {
//                        val chatEntry = messagesArray.getJSONObject(i)
//                        val friendName = chatEntry.getString("friendName")
//                        val chatArray = chatEntry.getJSONArray("chat")
//
//                        for (j in 0 until chatArray.length()) {
//                            val chatMessage = chatArray.getJSONObject(j)
//                            val chatTime = chatMessage.getString("chatTime")
//                            val content = chatMessage.getString("content")
//                            messageListArray.add(Message(friendName, content, chatTime))
//                        }
//                    }
//
//                    // Update the RecyclerView with the chat history
//                    val adapter = ChatAdapter(messageListArray)
//                    messageRecycler.adapter = adapter
//                    adapter.notifyDataSetChanged()
//
//                } catch (e: JSONException) {
//                    e.printStackTrace()
//                    // Handle parsing error if needed
//                }
//            },
//            { error ->
//                error.printStackTrace()
//                // Handle error if needed
//            }
//        )
//
//        // Add the request to the RequestQueue (Volley automatically manages the queue)
//        Volley.newRequestQueue(this).add(request)
//    }
//
//}



//third version


package com.aditya.adchatapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.SharedPreferences

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.aditya.adchatapp.adapter.ChatAdapter
import com.aditya.adchatapp.models.Message
import com.android.volley.Request
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import io.socket.client.IO
import io.socket.client.Socket
import io.socket.emitter.Emitter
import org.json.JSONException
import org.json.JSONObject
import java.net.URISyntaxException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

class ChatBoxActivity : AppCompatActivity() {

    private lateinit var chatLayout: ConstraintLayout
    private lateinit var socket: Socket
    private lateinit var messageRecycler: RecyclerView
    private lateinit var messageListArray: ArrayList<Message>
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var messageSendBTN: Button
    private lateinit var messageTXT: EditText
    private var receiverName: String = ""
    private var senderName: String = ""
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var senderMessages : ArrayList<Message>
    private lateinit var receiverMessages : ArrayList<Message>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_box)

        Log.d("ChatBoxActivity", "onCreate: Activity created")

        //Linking of UI Control of Chat Box Activity Layout
        chatLayout = findViewById(R.id.chboxactivity_layout)
        messageTXT = findViewById<EditText>(R.id.messageTXTED)
        messageSendBTN = findViewById<Button>(R.id.messageSentBTN)
        messageRecycler = findViewById<RecyclerView>(R.id.messageRV)

        messageRecycler.layoutManager = LinearLayoutManager(this)
        messageRecycler.addItemDecoration(DividerItemDecoration(this, LinearLayoutManager.VERTICAL))

        messageListArray = ArrayList()
        chatAdapter = ChatAdapter(messageListArray)
        messageRecycler.adapter = chatAdapter


        // Receive Value from MainActivity NickName value
        receiverName = intent.getStringExtra("FriendName").toString()


        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)

        senderName =  sharedPreferences.getString("senderName", "").toString()
        fetchChatHistoryFromDatabase(senderName,receiverName)

    }

    override fun onResume() {
        super.onResume()
        // Log activity resume
        Log.d("ChatBoxActivity", "onResume: Activity resumed")

        receiverName= intent.getStringExtra("FriendName").toString()

        Toast.makeText(this, receiverName, Toast.LENGTH_LONG).show()

        // Socket Connection
        try {
            socket = IO.socket("http://192.168.29.40:3000")
            socket.connect()
            socket.emit("join", senderName)
            Log.d("ChatBoxActivity", "Socket connected. Nickname: $receiverName")

        } catch (e: URISyntaxException) {
            e.printStackTrace()
        }

        // Display Snack Bar once anyone else Joins the chat
        socket.on("userjoinedthechat", Emitter.Listener { args ->
            val data = args[0].toString()
            runOnUiThread {
                val snackBar = Snackbar.make(chatLayout, data, Snackbar.LENGTH_LONG)
                snackBar.show()
            }
        })

        // Display Snack Bar when User disconnects
        socket.on("userdisconnect", Emitter.Listener { args ->
            val data = args[0].toString()
            runOnUiThread {
                val snackBar = Snackbar.make(chatLayout, data, Snackbar.LENGTH_LONG)
                snackBar.show()
            }
        })

        messageSendBTN.setOnClickListener {
            // Sending Message through Socket
            val messageValue = messageTXT.text.toString()
//            socket.emit("messagedetection")
            val messageData = JSONObject()
            val username = sharedPreferences.getString("senderName", "").toString()

            messageData.put("text", messageValue)
            messageData.put("from", username)
            messageData.put("to", receiverName)


            socket.emit("messagedetection",messageData)
            messageTXT.text.clear()


            val newMsg = Message(username, messageValue, Date()) // Assuming chatTime is the current time
            messageListArray.add(newMsg)
            chatAdapter.notifyItemInserted(messageListArray.size - 1)



        }

        socket.on("message", Emitter.Listener { args ->
            runOnUiThread {
                val data = args[0] as JSONObject
                try {
                    val senderNickname = data.getString("senderNickname")
                    val message = data.getString("message")
                    val chatTime = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'").parse(data.getString("chatTime"))

                    val newMsg = if (senderNickname == senderName) {
                        // If the message is from the sender, use the senderName as the nickname
                        Message(senderName, message, chatTime as Date)
                    } else {
                        // If the message is from other participants, use their nickname
                        Message(senderNickname, message, chatTime as Date)
                    }

                    messageListArray.add(newMsg)
                    chatAdapter.notifyDataSetChanged()

                } catch (e: JSONException) {
                    e.printStackTrace()
                }
            }
        })



    }

    override fun onDestroy() {
        super.onDestroy()
        socket.emit("userdisconnected", receiverName)
        socket.disconnect()
    }

    private fun fetchChatHistoryFromDatabase(senderUserName: String, receiverUserName: String) {
        val url = "http://192.168.29.40:3000/messages/$senderUserName/$receiverUserName"

        Log.d("sender","$senderUserName")
        Log.d("sender","$receiverUserName")

        val requestReceiver = JsonObjectRequest(
            Request.Method.GET, url, null,
            { response ->
                try {
                    val messages = response.getJSONObject("messages")
                    val chatsArray = messages.getJSONArray("chats")

                    val dateFormatter = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault())

                    for (i in 0 until chatsArray.length()) {
                        val chatMessage = chatsArray.getJSONObject(i)
                        val chatTimeStr = chatMessage.getString("createdAt")
                        val chatTime = dateFormatter.parse(chatTimeStr)
                        val content = chatMessage.getString("text")
                        val sender = chatMessage.getString("from")
                        messageListArray.add(Message(sender, content, chatTime))

                    }

                    Log.d("data","$messageListArray")
                    chatAdapter.notifyDataSetChanged()

                } catch (e: JSONException) {
                    e.printStackTrace()
                    // Handle JSON parsing error here
                }
            },
            { error ->
                error.printStackTrace()
                // Handle Volley error here
            }
        )

        Volley.newRequestQueue(this).add(requestReceiver)
    }


}

