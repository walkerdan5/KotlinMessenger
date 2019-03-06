package com.danielwalkerapp.kotlinmessenger

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatLogActivity : AppCompatActivity() {

    companion object {
        val TAG = "ChatLog"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)

        supportActionBar?.title = user.username

        setupDummyData()

        send_button_chatlog.setOnClickListener {
            Log.d(TAG, "Attempt to send message")
            performSendMessage()
        }
    }

    class ChatMessage(val id: String, val fromId: String, val toId: String, val text: String,
                      val timestamp: Long)

    private fun performSendMessage(){
        val text = editText_chatlog.text.toString()

        //allow us to save data into the db
        val reference = FirebaseDatabase.getInstance().getReference("/messages").push()
        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = user.uid

        if (fromId == null) return
        val chatMessage = ChatMessage(reference.key!!, text, fromId, toId, System.currentTimeMillis() / 1000 )
        reference.setValue(chatMessage)
                .addOnSuccessListener{
                    Log.d(TAG, "Save our chat message: ${reference.key}")

                }
    }

    private fun setupDummyData(){
        var adapter = GroupAdapter<ViewHolder>()
        adapter.add(ChatFromItem("from messaehgegfbhjdf"))
        adapter.add(ChatToItem("to messssssssage"))



        recyclerview_chatlog.adapter = adapter
    }

}



class ChatFromItem(val text: String): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_from_row.text = text
    }

    override fun getLayout(): Int {
        //row that renders our the chat messages
        return R.layout.chat_from_row

    }

}

class ChatToItem (val text: String): Item<ViewHolder>(){
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textView_to_row.text = text

    }

    override fun getLayout(): Int {
        //row that renders our the chat messages
        return R.layout.chat_to_row

    }

}
