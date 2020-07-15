package com.asusuigbo.frank.asusuigbo.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [LanguageInfo::class], version = 4, exportSchema = false)
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
                    ).addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
                        .build()
                }
            }
            return INSTANCE!!
        }

        private val MIGRATION_1_2: Migration = object : Migration(1, 2) {
            override fun migrate(database: SupportSQLiteDatabase) {
                // Remove the old table
                database.execSQL("DROP TABLE language_info");
                //re-create table
                database.execSQL("CREATE TABLE language_info " +
                        "(userId TEXT NOT NULL, language TEXT NOT NULL, is_active INTEGER NOT NULL, start_date TEXT NOT NULL, PRIMARY KEY(`userId`, `language`))")
            }
        }

        private val MIGRATION_2_3: Migration = object: Migration(2, 3){ // all that was changed was adding queries to Dao and modifying existing queries
            override fun migrate(database: SupportSQLiteDatabase) {}
        }

        private val MIGRATION_3_4: Migration = object: Migration(3, 4){ // added delete single language and made select all liveData
            override fun migrate(database: SupportSQLiteDatabase) {}
        }
    }


}


