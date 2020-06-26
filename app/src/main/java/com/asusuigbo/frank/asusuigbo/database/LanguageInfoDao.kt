package com.asusuigbo.frank.asusuigbo.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LanguageInfoDao {
    //No true/false in SQLite, so true maps to 1 and false maps to 0.
    @Query("SELECT * FROM language_info WHERE is_active = 1  AND userId = :userId LIMIT 1")
    suspend fun getActiveLanguage(userId: String): LanguageInfo?

    @Insert
    suspend fun addLanguage(languageInfo: LanguageInfo)

    @Query("SELECT * FROM language_info WHERE language = :language AND userId = :userId LIMIT 1")
    suspend fun getLanguage(userId: String, language: String): LanguageInfo?

    @Query("UPDATE language_info SET is_active = :notActive AND userId = :userId")
    suspend fun update(userId: String, notActive: Boolean)

    @Query("DELETE FROM language_info")
    suspend fun delete()
}