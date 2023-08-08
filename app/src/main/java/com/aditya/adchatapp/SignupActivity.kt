package com.aditya.adchatapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
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

class SignupActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        val nickNameTXT = findViewById<EditText>(R.id.nickNameEDTxt)
        val passwordTXT = findViewById<EditText>(R.id.passwordeditText)

        val signupBTN = findViewById<Button>(R.id.signupBTN)



        signupBTN.setOnClickListener {
            //Get Value from Nick Name Edit Text
            val nickNameValue = nickNameTXT.text.toString()
            val passwordValue = passwordTXT.text.toString()

            registerUserOnServer(nickNameValue,passwordValue)
            //Intent to send ChatBox Activity with Nick Name value

        }

    }


    private fun registerUserOnServer(nickname: String,password:String) {
        val client = OkHttpClient()

        val requestBody = "{\"nickname\": \"$nickname\", \"password\": \"$password\"}".toRequestBody("application/json".toMediaType())
        val request = Request.Builder()
            .url("http://192.168.29.40:3000/users/register") // Replace with your backend API URL for user registration
            .post(requestBody)
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





                val responseData = response.body?.string()
                try {
                    val jsonObject = JSONObject(responseData)

                    // Now you have the userId, you can use it for further operations

                    // The user is registered on the server
                    // Now, fetch the user list from the backend
                    val chatBoxIntent = Intent(this@SignupActivity, MainActivity::class.java)
                    startActivity(chatBoxIntent)

                } catch (e: JSONException) {
                    e.printStackTrace()
                }

            }
        })
    }
}