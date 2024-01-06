package com.balex.quiz.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.balex.quiz.data.pojo.Country
import com.balex.quiz.data.pojo.CountryNotUsedInQuiz

@Database(entities = [Country::class, CountryNotUsedInQuiz::class], version = 1, exportSchema = false)

abstract class CountriesDatabase : RoomDatabase()  {
    abstract fun countriesDao(): CountriesDao
}

