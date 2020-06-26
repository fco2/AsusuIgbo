package com.asusuigbo.frank.asusuigbo.database

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity(tableName = "language_info", primaryKeys = ["userId","language"])
data class LanguageInfo(
    @ColumnInfo(name = "userId")
    val userId: String,
    @ColumnInfo(name = "language")
    val language: String,
    @ColumnInfo(name = "is_active")
    val isActive: Boolean,
    @ColumnInfo(name = "start_date")
    val startDate: String
)