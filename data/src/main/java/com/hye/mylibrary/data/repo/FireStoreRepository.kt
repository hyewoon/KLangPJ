package com.hye.sesac.domain.firestore.repo

import android.annotation.SuppressLint
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.toObjects
import com.google.firebase.ktx.Firebase
import com.hye.mylibrary.data.model.FireStoreWords
import kotlinx.coroutines.tasks.await

class FireStoreRepository {
    @SuppressLint("SuspiciousIndentation")
    suspend fun testConnection(count: Long): List<FireStoreWords> {
        return try {
            val db = Firebase.firestore
            val task = db.collection("words1")
                .orderBy("korean", Query.Direction.ASCENDING)
                .limit(count)
                .get()
                .await()
                .toObjects<FireStoreWords>()
            println("FirebaseConnection + $task")
            println("FirebaseConnection + ${task.size}")

            task
        }catch (e:Exception){
            println(e.message.toString())
            emptyList<FireStoreWords>()
        }

    }
}