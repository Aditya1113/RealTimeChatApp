package com.aditya.adchatapp

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.aditya.adchatapp.adapter.ChatAdapter
import com.aditya.adchatapp.databinding.FragmentChatBinding
import com.aditya.adchatapp.models.Message
import io.socket.client.Socket

/**
 * An example full-screen fragment that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
class ChatFragment : Fragment() {

    private lateinit var messageRecycler: RecyclerView
    private lateinit var messageListArray: ArrayList<Message>
    private lateinit var chatAdapter: ChatAdapter
    private lateinit var messageTXT: EditText
    private lateinit var messageSendBTN: Button
    private lateinit var socket: Socket
    private var receiverName: String = ""

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_chat, container, false)

        // Initialize UI components and other variables
        messageRecycler = view.findViewById(R.id.messageRV)
        messageTXT = view.findViewById(R.id.messageTXTED)
        messageSendBTN = view.findViewById(R.id.messageSentBTN)
        // ...

        return view
    }

    override fun onResume() {
        super.onResume()
        // Connect to socket, set up event listeners, etc.
        // (similar to the code you have in your activity)
    }

    override fun onDestroy() {
        super.onDestroy()
        // Disconnect socket, clean up resources, etc.
        // (similar to the code you have in your activity)
    }

    // Add methods for sending messages, handling received messages, etc.
}