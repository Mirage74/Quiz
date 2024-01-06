package com.balex.quiz.data.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.balex.quiz.data.pojo.Country
import io.reactivex.rxjava3.core.Completable

@Dao
interface CountriesDao {
    @Query("SELECT * FROM countries")
    fun getAllCountries(): List<Country>

    @Query("SELECT * FROM countries_not_used_in_quiz")
    fun getAllCountriesNotUseInQuiz(): LiveData<List<Country>>

    @Query("SELECT * FROM countries_not_used_in_quiz WHERE id = :countryId")
    fun getCountry(countryId: Int): Country

//    @Query("INSERT  ")
//    fun fillNotUsedCountryTable(): Completable

    @Query("DELETE FROM countries_not_used_in_quiz WHERE id = :countryId")
    fun removeCountryFromNotUsed(countryId: Int): Completable

    @Query("DELETE FROM countries_not_used_in_quiz")
    fun clearNotUsedCountriesTable()
}