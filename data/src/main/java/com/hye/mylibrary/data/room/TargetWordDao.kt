package com.hye.mylibrary.data.room

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import java.util.Date

/**
 * 개별 테이블별로 데이터 insert하고 , 그걸 바탕으로 AllInfo entity에 데이터를 넣고,
 * 전체 데이터 불러올때는 AllInfo entity를 가져오기
 */
@Dao
interface TargetWordDao {

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTargetWord(targetWord: TargetWord)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTargetWordExampleInfo(exampleInfo: List<TargetWordExampleInfo>)

    @Transaction
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTargetWordPronunciationInfo(pronunciationInfo: List<TargetWordPronunciationInfo>)

    @Transaction
    suspend fun insertLimitedTargetWordExampleInfo(
        targetWord: TargetWord,
        exampleInfo: List<TargetWordExampleInfo>,
        pronunciationInfo: List<TargetWordPronunciationInfo>
    ) {
        insertTargetWord(targetWord)

        //제한된 갯수의 데이터만 가져와서, firestore에서 받은 데이터와 일치시키기
        val limitedExamples = exampleInfo
            .groupBy { it.type }
            .mapValues { (_, examples) -> examples.take(3) }
            .values
            .flatten()

        insertTargetWordExampleInfo(limitedExamples)
        insertTargetWordPronunciationInfo(pronunciationInfo)
    }


    @Transaction
    suspend fun insertTargetWordWithAllInfo(
        targetWord: TargetWord,
        exampleInfo: List<TargetWordExampleInfo>,
        pronunciationInfo: List<TargetWordPronunciationInfo>
    ) {


        insertTargetWord(targetWord)
        insertTargetWordExampleInfo(exampleInfo)
        insertTargetWordPronunciationInfo(pronunciationInfo)
    }

    @Transaction
    @Query("DELETE FROM pronunciation_info WHERE pronunciation_targetCode_fk = :targetCode")
    suspend fun deletePronunciationInfo(targetCode: Long)

    @Transaction
    @Query("DELETE FROM example_info WHERE example_targetCode_fk = :targetCode")
    suspend fun deleteExampleInfo(targetCode: Long)

    @Transaction
    @Query("DELETE FROM target_word")
    suspend fun deleteAll()


    @Query("SELECT* FROM target_word WHERE todayString = :todayString")
    suspend fun searchTargetWordByDate(todayString: String): List<TargetWordWithAllInfo>

    @Query("SELECT * FROM target_word")
    suspend fun getAllTargetWords(): List<TargetWordWithAllInfo>
}