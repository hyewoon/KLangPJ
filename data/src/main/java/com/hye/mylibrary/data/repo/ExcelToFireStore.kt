package com.hye.sesac.domain.firestore.repo
import android.content.Context
import com.google.firebase.firestore.WriteBatch
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hye.data.firestore.model.FirebaseResult
import kotlinx.coroutines.tasks.await
import org.apache.poi.ss.usermodel.Row
import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.FileNotFoundException


class ExcelToFireStore( val context: Context) {

    private val db = Firebase.firestore

    companion object {
        private const val FIREBASE_COLLECTION_NAME = "words1"
        private const val BASH_SIZE = 500
        private const val EXCEL_FILE_NAME = "topik_basic_words.xls"
    }


    suspend fun uploadExcelToFireStoreWithBatch(): FirebaseResult<Unit> {
        return try {

            //파일 있는지 확인
            context.assets.open(EXCEL_FILE_NAME).use {
                println("파일 있음")
                readExcelFile()
                FirebaseResult.Success(Unit)

            }

        } catch (e: FileNotFoundException) {
            FirebaseResult.Failure(Exception("Excel file not found"))
        } catch (e: Exception) {
            FirebaseResult.Failure(e)
        }


    }

    /**
     * assets에 추가한 엑셀 파일을 읽고 데이터를 추출
     *
     */
    private suspend fun readExcelFile() {
        val inputStream = context.assets.open(EXCEL_FILE_NAME)
        val workbook = WorkbookFactory.create(inputStream)
        val sheet = workbook.getSheetAt(0)

        workbook.use {
            val wordDataList = mutableListOf<HashMap<String, Any>>()

            for (row in sheet) {
                if (row.rowNum == 0) continue

                val wordData = mapRowToWordData(row)
                wordDataList.add(wordData)
            }

            uploadFireStoreWithBatch(wordDataList)
        }
    }

    /**
     * 데이터 매칭
     */
    private fun mapRowToWordData(row: Row): HashMap<String, Any> {
        val korean = row.getCell(0)?.toString() ?: ""
        val pos = row.getCell(1)?.toString() ?: ""
        val frequency = row.getCell(2)?.numericCellValue?.toInt() ?: 0
        val english = row.getCell(3)?.toString() ?: ""
        val targetCode = row.getCell(4)?.toString() ?: ""
        val wordGrade = row.getCell(5)?.toString() ?: ""
        val pronunciation = row.getCell(6)?.toString() ?: ""
        val example = row.getCell(7)?.toString() ?: ""

        val wordData = hashMapOf<String, Any>(
            "korean" to korean,
            "pos" to pos,
            "frequency" to frequency,
            "english" to english,
            "targetCode" to targetCode,
            "wordGrade" to wordGrade,
            "pronunciation" to pronunciation,
            "example" to example
        )

        return wordData
    }

    /**
     * fireStore에 대량 데이터 처리시 사용하는 batch만들기 쓰기 작업 -> writeBatch
     * 문서참조 만들후 배치 단위로  데이터  추가
     */
    private suspend fun uploadFireStoreWithBatch(wordDataList: List<HashMap<String, Any>>) {

        var batch: WriteBatch = db.batch()
        var counter = 0//배치에 추가한 작업 수 카운드
        var batchCount = 1// 배치 묶음(500개씩 작업시 몇 묶음 진행했는지 확인용)

        wordDataList.forEach {
            val docRef = db.collection(FIREBASE_COLLECTION_NAME).document()
            batch.set(docRef, it)
            counter++

            //500개 단위로 배치 처리
            if (counter == BASH_SIZE) {
                try {

                    batch.commit().await()

                    println("batchCount: $batchCount 완료")

                    //새로운 배치 시작 및 카운터 초기화
                    batch = db.batch()
                    counter = 0
                    batchCount++
                } catch (e: Exception) {
                    println("batchCount: $batchCount 실패 ${e.message}")
                    throw e
                }

            }

        }

        //마지막 배치 처리
        if (counter > 0) {
            try {
                batch.commit().await()
            } catch (e: Exception) {
                println("마지막 batchCount: $batchCount 실패 ${e.message}")
                throw e
            }


        }
    }
}
