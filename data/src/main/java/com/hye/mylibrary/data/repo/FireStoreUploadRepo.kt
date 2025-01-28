package com.hye.sesac.domain.firestore.repo

import android.content.Context
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hye.sesac.data.rest.RetrofitServiceInstance
import kotlinx.coroutines.tasks.await


class FireStoreUploadRepo(val context: Context, private val apiKey: String) {
    private val db = Firebase.firestore
    private val retrofitService = RetrofitServiceInstance.getRetrofitServiceInstance()
    private val excelToFireStore = ExcelToFireStore(context)
    private val apiToFireStore = ApiToFireStore(retrofitService, apiKey)

    private val updateStatusRef = db.collection("updateStatus")

/*
    //작업 완료되었는지 확인
    suspend fun isTaskCompleted(type: String){
        when(type) {
            "excel" -> {
                isExcelTaskCompleted()

            }
            "api" -> isApiTaskCompleted()
        }

    }
*/

    //1.작업 완료 여부 확인
    suspend fun isExcelTaskCompleted(): Boolean {
        val document = updateStatusRef.document("excelTask").get().await()
        return document.getBoolean("isCompleted") ?: false
    }

    //2. 데이터 업데이트
    suspend fun upLoadExcelToFireStore() = excelToFireStore.uploadExcelToFireStoreWithBatch()

    //3. 작업 완료 업데이트
    suspend fun markAllTasksAsCompleted() {
        updateStatusRef.document("excelTask").set(hashMapOf("isCompleted" to true)).await()
    }

    suspend fun isApiTaskCompleted(): Boolean {
        val document = updateStatusRef.document("apiTask").get().await()
        return document.getBoolean("isCompleted") ?: false

    }

    suspend fun upLoadApiToFireStore() = apiToFireStore.uploadDataFromApiToFireStore()

    suspend fun markAllApiTasksAsCompleted() {
        updateStatusRef.document("apiTask").set(hashMapOf("isCompleted" to true)).await()

    }

}