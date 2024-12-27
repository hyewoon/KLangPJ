package com.hye.sesac.klangpj

import android.content.Context
import android.util.Log
import com.google.firebase.Firebase
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.firestore.firestore
import org.apache.poi.ss.usermodel.WorkbookFactory

const val TAG = "ExcelToFireStore"
class ExcelToFireStore(val context: Context) {

    private val firestore = Firebase.firestore

   fun uploadExcelToFireStoreWithBatch() {
        try {
            /**
             * 1. excel 파일 열기
             */
            //val uri = Uri.fromFile(File("C:\\sesac_android\\topik_basic_words.xls"))
            //val inputStream = context.contentResolver.openInputStream(uri)
            val inputStream = context.assets.open("topik_basic_words.xls")
            val workbook = WorkbookFactory.create(inputStream)
            val sheet = workbook.getSheetAt(0) // 첫 번째 시트를 읽음

            var batch: WriteBatch = firestore.batch()
            var counter = 0 // Batch에 추가한 작업 수 카운터
            var batchCount = 1 // Batch 묶음 카운터


            for (row in sheet) {
                // 첫 번째 행은 헤더라면 스킵
                if (row.rowNum == 0) continue

                /**
                 * 2. 엑셀데이터 -> hashMap
                 */

                //한글단어
                val korean = row.getCell(0)?.toString() ?: ""
                //품사
                val wordClass = row.getCell(1)?.toString() ?: ""
                // 사용빈도수
                val frequency = row.getCell(2)?.numericCellValue?.toInt() ?: 0
                //영어단어
                val english = row.getCell(3)?.toString() ?: ""


                val wordData = hashMapOf(
                    "korean" to korean,
                    "wordClass" to wordClass,
                    "frequency" to frequency,
                    "english" to english
                )

                /**
                 * 3. 새로운 문서 참조 생성 후 Batch에 추가
                 */

                val docRef = firestore.collection("words").document()
                batch.set(docRef, wordData)
                counter++

                // 500개 단위로 Batch 커밋
                if (counter == 500) {
                    Log.d(TAG, "Batch $batchCount 실행 중...")
                    println("Batch $batchCount 실행 중...")
                    batch.commit().addOnSuccessListener {
                        Log.d(TAG, "Batch $batchCount 완료")
                    }.addOnFailureListener { e ->
                        Log.d(TAG, "Batch $batchCount 실패: $e")
                    }

                    // 새로운 Batch 시작
                    batch = firestore.batch()
                    counter = 0
                    batchCount++
                }
            }

            // 마지막 Batch 처리
            if (counter > 0) {
                Log.d(TAG, "마지막 Batch ")
                batch.commit().addOnSuccessListener {
                    Log.d(TAG, "마지막 Batch 완료!")

                }.addOnFailureListener { e ->
                    Log.d(TAG, "마지막 Batch 실패: $e")

                }
            }

            workbook.close()
        } catch (e: Exception) {
            Log.e("TAG", e.toString())
        }
    }
}