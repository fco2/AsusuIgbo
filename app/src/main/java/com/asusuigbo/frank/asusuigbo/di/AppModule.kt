package com.asusuigbo.frank.asusuigbo.di

import android.content.Context
import androidx.room.Room
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.asusuigbo.frank.asusuigbo.database.AsusuIgboDatabase
import com.asusuigbo.frank.asusuigbo.database.LanguageInfoDao
import com.asusuigbo.frank.asusuigbo.helpers.Constants.DATABASE_NAME
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ApplicationComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
@InstallIn(ApplicationComponent::class)
object AppModule {

    @Provides
    @Singleton  // Scope of function, they is also @ActivityScoped and more..
    fun provideDatabase(@ApplicationContext context: Context) = Room.databaseBuilder(context, AsusuIgboDatabase::class.java, DATABASE_NAME)
        .addMigrations(MIGRATION_1_2, MIGRATION_2_3, MIGRATION_3_4)
        .build()

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

    @Provides
    @Singleton
    fun provideLanguageInfoDao(db: AsusuIgboDatabase): LanguageInfoDao = db.languageInfoDao

    @Provides
    @Singleton
    @UserId
    fun provideUserId(): String = FirebaseAuth.getInstance().currentUser!!.uid

    @Qualifier
    @Retention(AnnotationRetention.BINARY)
    annotation class UserId
}