package com.hye.sesac.klangpj.data.preferences

import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey

object PreferenceDataStoreConstants {
    //출석 관련
    val ATTENDANCE_DATE =  stringPreferencesKey("attendance_date")
    val IS_ATTENDED_TODAY = booleanPreferencesKey("is_attended_today")

    val ATTENDANCE_MONDAY = booleanPreferencesKey("attendance_monday")
    val ATTENDANCE_TUESDAY = booleanPreferencesKey("attendance_tuesday")
    val ATTENDANCE_WEDNESDAY = booleanPreferencesKey("attendance_wednesday")
    val ATTENDANCE_THURSDAY = booleanPreferencesKey("attendance_thursday")
    val ATTENDANCE_FRIDAY = booleanPreferencesKey("attendance_friday")
    val ATTENDANCE_SATURDAY = booleanPreferencesKey("attendance_saturday")
    val ATTENDANCE_SUNDAY = booleanPreferencesKey("attendance_sunday")

    val WEEKLY_ATTENDANCE_COUNT = intPreferencesKey("weekly_attendance_count")
    val LAST_ATTENDANCE = stringPreferencesKey("last_attendance")


    //단어 학습 관련
    val TARGET_WORD_COUNT = intPreferencesKey("target_word_count")
    val CURRENT_WORD_COUNT = intPreferencesKey("current_word-count")

    //포인트 관련
    val PAW_POINT = intPreferencesKey("paw_point")
    val TARGET_POINT = intPreferencesKey("target_point")

    //설정 관련
    val PUSH_NOTIFICATION = booleanPreferencesKey("push_notification")



}