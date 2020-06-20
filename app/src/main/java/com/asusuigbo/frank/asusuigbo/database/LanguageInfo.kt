package com.asusuigbo.frank.asusuigbo.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "language_info")
data class LanguageInfo(
    @PrimaryKey
    @ColumnInfo(name = "language")
    val Language: String,
    @ColumnInfo(name = "is_active")
    val isActive: Boolean,
    @ColumnInfo(name = "start_date")
    val startDate: String
)