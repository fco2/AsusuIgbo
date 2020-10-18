package com.asusuigbo.frank.asusuigbo.database

import androidx.room.Database
import androidx.room.RoomDatabase


@Database(entities = [LanguageInfo::class], version = 4, exportSchema = false)
abstract class AsusuIgboDatabase : RoomDatabase() {
    abstract val languageInfoDao: LanguageInfoDao
}


