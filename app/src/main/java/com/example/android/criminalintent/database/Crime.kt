package com.example.android.criminalintent.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.text.DateFormat
import java.util.*

@Entity
data class Crime(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0L,

    @ColumnInfo(name = "title")
    var title: String = "",

    @ColumnInfo(name = "date")
    var date: Date = Date(),

    @ColumnInfo(name = "solved")
    var isSolved: Boolean = false,

    @ColumnInfo(name = "suspect")
    var suspect: String = ""
) {
    val photoFileName
        get() = "IMG_$id.jpg"
}
