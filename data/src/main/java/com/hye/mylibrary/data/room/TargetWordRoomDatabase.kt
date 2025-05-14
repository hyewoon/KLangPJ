package com.hye.mylibrary.data.room

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(
    entities = [TargetWord::class, TargetWordExampleInfo::class, TargetWordPronunciationInfo::class],
    version = 6
)
abstract class TargetWordRoomDatabase : RoomDatabase() {

    abstract fun targetWordDao(): TargetWordDao

    companion object {
        private lateinit var INSTANCE: TargetWordRoomDatabase
        fun getDatabase(context: Context): TargetWordRoomDatabase {
            if (!this::INSTANCE.isInitialized) {
                synchronized(TargetWordRoomDatabase::class.java) {
                    INSTANCE =
                        Room.databaseBuilder(
                            context.applicationContext,
                            TargetWordRoomDatabase::class.java,
                            "target_word_database"
                        )
                            .fallbackToDestructiveMigration()
                            .build()
                }

            }
            return INSTANCE
        }

    }
}