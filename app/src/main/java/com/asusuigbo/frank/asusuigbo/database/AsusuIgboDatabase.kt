package com.asusuigbo.frank.asusuigbo.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [LanguageInfo::class], version = 1)
abstract class AsusuIgboDatabase : RoomDatabase() {

}
