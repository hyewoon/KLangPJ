package com.hye.sesac.domain.firestore.datasource

/*
import com.google.firebase.Firebase
import com.google.firebase.firestore.firestore
import com.hye.sesac.domain.firestore.Entity.FireStoreWords
import kotlinx.coroutines.tasks.await

object FireBaseDataSource {

    private val db = Firebase.firestore
    suspend fun read(): MutableList<FireStoreWords> {
        return try {

            val snapshot = db.collection("words").get().await()
            db.collection("words").get().await()

            if (!snapshot.isEmpty) {
                snapshot.toObjects(FireStoreWords::class.java)
            } else {
                mutableListOf()
            }

        } catch (e: Exception) {

            // 에러 로깅
            println("Firestore 읽기 에러: ${e.message}")
            mutableListOf()

        }


    }
    fun testConnection() {
        db.collection("words")
            .get()
            .addOnSuccessListener {
                println("Firestore 연결 성공: ${it.size()} 문서 발견")
            }
            .addOnFailureListener { e ->
                println("Firestore 연결 실패: ${e.message}")
            }
    }
}*/
