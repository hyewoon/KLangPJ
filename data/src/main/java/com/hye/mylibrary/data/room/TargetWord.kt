package com.hye.mylibrary.data.room

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import androidx.room.Relation
import java.util.Date

/**
 * 중첩된 데이터 구조를 3개의 테이블로 만들고, foreignKey 연결 및 relation연결
 * 3개 테이블의 합친 AllInfo entity생성
 */
@Entity(tableName = "target_word")
data class TargetWord(
    @PrimaryKey val targetCode: Long = 0L,
    val frequency: Long? = 0L,
    val korean: String = "",
    val english: String = "",
    val pos: String = "",
    val wordGrade: String? = "",
    val timeStamp: Long= 0L,
    val todayString: String =""
)

@Entity(
    tableName = "example_info",
    foreignKeys = [
        ForeignKey(
            entity = TargetWord::class,
            parentColumns = ["targetCode"],
            childColumns = ["example_targetCode_fk"],
            onDelete = ForeignKey.CASCADE

            )
    ],
    indices = [androidx.room.Index("example_targetCode_fk")]
)
data class TargetWordExampleInfo(
    @PrimaryKey(autoGenerate = true)
    val exampleId: Int = 0,
    @ColumnInfo(name = "example_targetCode_fk")
    val targetCode: Long = 0,
    val type: String = "",
    val example: String = "",
)

@Entity(
    tableName = "pronunciation_info",
    foreignKeys = [
        ForeignKey(
            entity = TargetWord::class,
            parentColumns = ["targetCode"],
            childColumns = ["pronunciation_targetCode_fk"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [androidx.room.Index("pronunciation_targetCode_fk")]
)
data class TargetWordPronunciationInfo(
    @PrimaryKey(autoGenerate = true)
    val pronunciationId: Int = 0,
    @ColumnInfo(name = "pronunciation_targetCode_fk")
    val targetCode: Long = 0,
    val pronunciation: String? = "",
    val audioUrl: String? = "",
)

data class TargetWordWithAllInfo(
    @Embedded val targetWord: TargetWord,
    @Relation(
        parentColumn = "targetCode",
        entityColumn = "example_targetCode_fk"
    )
    val exampleInfo: List<TargetWordExampleInfo>,

    @Relation(
        parentColumn = "targetCode",
        entityColumn = "pronunciation_targetCode_fk"
    )
    val pronunciationInfo: List<TargetWordPronunciationInfo>,


    )