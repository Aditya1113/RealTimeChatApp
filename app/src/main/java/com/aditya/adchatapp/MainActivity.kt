package com.aditya.adchatapp


import android.content.Context
import android.content.SharedPreferences
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.gson.Gson
import okhttp3.Call
import okhttp3.Callback
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.Response
import org.json.JSONException
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {
    private lateinit var friendId : String
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Linking of UI Component of Activity_Main.xml
        val nickNameTXT = findViewById<EditText>(R.id.nickNameEDTxt)
        val passwordTXT = findViewById<EditText>(R.id.passwordeditText)
        val goToBTN = findViewById<Button>(R.id.goToChatBTN)
        val signUpTextView = findViewById<TextView>(R.id.signUpTextView)
        sharedPreferences = getSharedPreferences("MyPrefs", Context.MODE_PRIVATE)


        //GoTOChat Button on Click Listner
        goToBTN.setOnClickListener {
            //Get Value from Nick Name Edit Text
            val sendernickNameValue = nickNameTXT.text.toString()
            val passwordValue = passwordTXT.text.toString()
            //Intent to send ChatBox Activity with Nick Name value

            // Call the login function
            loginUserOnServer(sendernickNameValue, passwordValue)
        }
        
        
        
        signUpTextView.setOnClickListener {
            // Start SignUpActivity when "Sign up" text is clicked
            val signUpIntent = Intent(this, SignupActivity::class.java)
            startActivity(signUpIntent)
        }
    }


    override fun onDestroy() {
        super.onDestroy()
        val editor = sharedPreferences.edit()
        editor.remove("username")
        editor.apply()
    }


    private fun loginUserOnServer(sendernickname: String, password: String) {

        val loginUrl = "http://192.168.29.40:3000/users/login"

        val client = OkHttpClient()

        val requestBody = "{\"username\": \"$sendernickname\", \"password\": \"$password\"}".toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url(loginUrl)
            .post(requestBody)
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                // Handle failure, e.g., show an error message or retry logic
            }

            override fun onResponse(call: Call, response: Response) {

                val responseBody = response.body?.string()
//                Log.d("get","$responseBody")

//                if (!response.isSuccessful) {
//                    // Handle non-successful response, e.g., show an error message
//                Log.d("Res","$response")
//                    return
//                }


                try {
                    val jsonObject = JSONObject(responseBody)
                    val message = jsonObject.getString("message")

                    // Now 'message' contains the value of the "message" field from the JSON response
                    // You can use this value as needed.
                    Log.d("Message", message)

                    // Check the "message" value to determine the next actions
                    if (message == "Login successful") {
                        // The user exists in the database, proceed to the UsersActivity
                        val userListIntent = Intent(this@MainActivity, UsersActivity::class.java)
//                        userListIntent.putExtra("senderNickName", sendernickname)

                        // Store the username in shared preferences
                        val editor = sharedPreferences.edit()
                        editor.putString("senderName", sendernickname)
                        editor.apply()

                        startActivity(userListIntent)
                    } else {
                        // Invalid credentials, show an error message to the user
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "Invalid Credentials", Toast.LENGTH_SHORT).show()
                        }
                    }

                } catch (e: JSONException) {
                    Log.e("JSON Parsing Error", e.toString())
                }


            }
        })
    }
}