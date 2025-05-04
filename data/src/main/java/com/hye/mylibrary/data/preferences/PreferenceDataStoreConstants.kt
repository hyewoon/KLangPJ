package com.hye.mylibrary.data.preferences

import androidx.datastore.preferences.core.Preferences
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


    //단어 다운 로드 관련(firestore)
    val TARGET_CODE =intPreferencesKey("target_code")
    val DOCUMENT_ID = stringPreferencesKey("document_id")

    /*
    * 단어학습 관련
    * TARGET_WORD_COUNT: 오늘 학습 할 단어 수
    * CURRENT_WORD_COUNT: 현재 학습 한 단어 수
    * */
    val TARGET_WORD_COUNT = intPreferencesKey("target_word_count")
    val CURRENT_WORD_COUNT = intPreferencesKey("current_word-count")

    /*
    * 포인트 관련
    *  PAW_POINT(누적): 현재 까지 학습한 단어 수
    *  TARGET_POINT(누적): 현재 까지 적립된 포인트
    * */
    val PAW_POINT = intPreferencesKey("paw_point")
    val TARGET_POINT = intPreferencesKey("target_point")

    /*
    * PUSH_NOTIFICATION: 알림 설정
    *  LANGUAGE_SETTING: 언어 설정
    * */
    val PUSH_NOTIFICATION = booleanPreferencesKey("push_notification")
    val LANGUAGE_SETTING: Preferences.Key<String> = stringPreferencesKey("language_setting")



}