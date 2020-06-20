package com.asusuigbo.frank.asusuigbo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [LanguageInfo::class], version = 1, exportSchema = false)
abstract class AsusuIgboDatabase : RoomDatabase() {
    abstract val languageInfoDao: LanguageInfoDao

    companion object{
        @Volatile
        private var INSTANCE: AsusuIgboDatabase? = null

        fun getDatabase(context: Context) : AsusuIgboDatabase{
            synchronized(this){
                if(INSTANCE == null){
                    INSTANCE = Room.databaseBuilder(
                        context.applicationContext, AsusuIgboDatabase::class.java, "asusu_igbo_database"
                    ).build()
                }
            }
            return INSTANCE!!
        }
    }
}
