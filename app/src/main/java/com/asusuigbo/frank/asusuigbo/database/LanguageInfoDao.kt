package com.asusuigbo.frank.asusuigbo.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface LanguageInfoDao {
    //No true/false in SQLite, so true maps to 1 and false maps to 0.
    @Query("SELECT * FROM language_info WHERE is_active = 1 LIMIT 1")
    suspend fun getActiveLanguage(): LanguageInfo?

    @Insert
    suspend fun addLanguage(languageInfo: LanguageInfo)

    @Query("SELECT * FROM language_info WHERE language = :language LIMIT 1")
    suspend fun getLanguage(language: String): LanguageInfo?

    @Query("UPDATE language_info SET is_active = :not_active")
    suspend fun update(not_active: Boolean)

    @Query("DELETE FROM language_info")
    suspend fun delete()
}