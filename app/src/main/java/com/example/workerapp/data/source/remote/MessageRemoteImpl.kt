package com.example.workerapp.data.source.remote

import android.util.Log
import com.example.workerapp.data.source.MessageDataSource
import com.example.workerapp.data.source.model.MessageModel
import com.example.workerapp.data.source.model.RoomModel
import com.example.workerapp.data.source.remote.dto.NetworkResult
import com.google.firebase.Firebase
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class MessageRemoteImpl @Inject constructor() : MessageDataSource.Remote {
    private val roomRef = FirebaseDatabase.getInstance().reference.child(ROOM_PATH)
    private val chatRef = Firebase.database.reference.child(CONVERSATION_PATH)
    private var messageListener: ChildEventListener? = null


    override suspend fun getAllConversations(currentUserUid: String): Flow<List<RoomModel>> =
        callbackFlow {
            val listener = object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val res = snapshot.children.mapNotNull { roomSnapshot ->
                        val roomId = roomSnapshot.key ?: return@mapNotNull null

                        if (roomId.split("_").contains(currentUserUid)) {
                            roomSnapshot.getValue(RoomModel::class.java)?.copy(roomId = roomId)
                        } else null
                    }

                    trySend(res.sortedByDescending { it.lastTimestamp })
                    Log.d(TAG, "Emitted ${res.size} conversations for $currentUserUid")
                }

                override fun onCancelled(error: DatabaseError) {
                    Log.e(TAG, "Firebase cancelled: ${error.message}")
                    close(error.toException())
                }
            }

            // add listener
            roomRef.addValueEventListener(listener)

            //close
            awaitClose {
                Log.d(TAG, "Listener removed (flow closed)")
                roomRef.removeEventListener(listener)
            }
        }

    override suspend fun observeMessages(conversationId: String): Flow<MessageModel> =
        callbackFlow {
            val listener = object : ChildEventListener {
                override fun onChildAdded(
                    snapshot: DataSnapshot,
                    previousChildName: String?
                ) {
                    val messageId = snapshot.key ?: return

                    val message = snapshot.getValue(MessageModel::class.java)?.copy(id = messageId)

                    message?.let { msg ->
                        Log.d(TAG, "Emitted new message in $conversationId: $msg")
                        trySend(msg)
                    }
                }

                override fun onChildChanged(
                    snapshot: DataSnapshot,
                    previousChildName: String?
                ) {
                    TODO("Not yet implemented")
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                    TODO("Not yet implemented")
                }

                override fun onChildMoved(
                    snapshot: DataSnapshot,
                    previousChildName: String?
                ) {
                    TODO("Not yet implemented")
                }

                override fun onCancelled(error: DatabaseError) {
                    TODO("Not yet implemented")
                }
            }

            chatRef.child(conversationId).addChildEventListener(listener)

            awaitClose {
                Log.d(TAG, "Message listener removed for $conversationId (flow closed)")
                chatRef.child(conversationId).removeEventListener(listener)
            }

        }

    override suspend fun sendMsg(
        conversationId: String,
        messageModel: MessageModel
    ): NetworkResult<Unit> = try{
        val temp = mapOf(
            "message" to messageModel.message,
            "timestamp" to messageModel.timestamp,
            "receiverId" to messageModel.receiverId,
            "senderId" to messageModel.senderId
        )

        chatRef.child(conversationId)
            .child(messageModel.id)
            .setValue(temp)
            .await()

        roomRef.child(conversationId)
            .updateChildren(
                mapOf(
                    "lastMessage" to messageModel.message,
                    "lastTimestamp" to messageModel.timestamp
                )
            ).await()

        NetworkResult.Success(Unit)
    }catch (e : Exception){
        Log.e("MessageRemoteImpl", "sendMsg error", e)
        NetworkResult.Error(e.message ?: "Unknown error")
    }

    override suspend fun deleteMessageListener(conversationId: String) {
        messageListener?.let {
            chatRef.child(conversationId).removeEventListener(it)
            messageListener = null
        }
    }

    companion object {
        private const val ROOM_PATH = "rooms"
        private const val CONVERSATION_PATH = "conversations"
        private const val TAG = "MessageRemoteImpl"
    }
}
