package com.example.android.criminalintent.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase


@Database(entities = [Crime::class], version = 3, exportSchema = false)
@TypeConverters(CrimeTypeConverters::class)
abstract class CrimeDatabase : RoomDatabase() {



    abstract val crimeDatabaseDao: CrimeDao

    companion object {

        @Volatile
        private var INSTANCE: CrimeDatabase? = null

        fun getInstance(context: Context): CrimeDatabase {
            synchronized(this) {
                var instance = INSTANCE

                if (instance == null) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        CrimeDatabase::class.java,
                        "crime_history_database"
                    )
                        .addMigrations(migration_2_3)
                        .build()
                    INSTANCE = instance
                }
                return instance
            }
        }

        private val migration_2_3 = object : Migration(2, 3) {
            override fun migrate(database: SupportSQLiteDatabase) {
                database.execSQL(
                    "ALTER TABLE Crime ADD COLUMN suspect TEXT NOT NULL DEFAULT ''"
                )
            }
        }
    }
}