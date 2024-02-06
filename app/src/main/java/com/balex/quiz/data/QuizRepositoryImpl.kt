package com.balex.quiz.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.balex.quiz.R
import com.balex.quiz.data.api.ApiFactory
import com.balex.quiz.domain.entity.Country
import com.balex.quiz.domain.entity.GameSettings
import com.balex.quiz.domain.entity.Level
import com.balex.quiz.domain.entity.Question
import com.balex.quiz.domain.repository.QuizRepository
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.Collections
import java.util.Random
import java.util.stream.Collectors


object QuizRepositoryImpl : QuizRepository {
    init {
        getCountriesListFromBackend()
    }


    private val countriesListFull_LD = MutableLiveData<List<Country>>()
    private val countriesListNotUsedInQuiz_LD = MutableLiveData<List<Country>>()
    private var listOddQuestions: List<Country> = Collections.emptyList()
    private var listEvenQuestions: List<Country> = Collections.emptyList()
    private val compositeDisposable = CompositeDisposable()
    private const val NUMBER_ANSWER_OPTIONS = 4
    private const val DIFFICULT_LEVEL_EASY = 0
    private const val DIFFICULT_LEVEL_MIDDLE = 1
    private const val DIFFICULT_LEVEL_HARD = 2
    private val rand = Random()


    private fun getCountriesListFromBackend() {
        CoroutineScope(Dispatchers.IO).launch {
            compositeDisposable.add(
                ApiFactory.apiService.loadCountries()
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({
                        countriesListFull_LD.value =
                            it.countries.stream().sorted { o1, o2 -> o1.id.compareTo(o2.id) }
                                .collect(
                                    Collectors.toList()
                                )
                        countriesListNotUsedInQuiz_LD.value = countriesListFull_LD.value
                    }) {
                        throw (RuntimeException("Error get countries list from server: + $it"))
                    })
        }

    }


    override fun getCountriesListFullRepository(): LiveData<List<Country>> {
        return countriesListFull_LD
    }

    override fun getCountriesListNotUsedRepository(): LiveData<List<Country>> {
        return countriesListNotUsedInQuiz_LD
    }

    override fun deleteCountryFromNotUsedListRepository(country: Country) {
        countriesListNotUsedInQuiz_LD.value =
            countriesListNotUsedInQuiz_LD.value?.stream()?.filter { e ->
                e != country
            }?.collect(Collectors.toList())

    }

    override fun getGameSettings(level: Level): GameSettings {
        val allQuestions = R.integer.test_questions
        val timeMaxSec = R.integer.max_time_test_sec
        val timeRestBestScoreSec = R.integer.time_score_best
        val timeRestMediumScoreSec = R.integer.time_score_medium

        return when (level) {
            Level.EASY -> {
                GameSettings(
                    allQuestions,
                    timeMaxSec,
                    timeRestBestScoreSec,
                    timeRestMediumScoreSec,
                    R.integer.easy_points_max,
                    R.integer.easy_points_medium,
                    R.integer.easy_points_min
                )
            }

            Level.MEDIUM -> {
                GameSettings(
                    allQuestions,
                    timeMaxSec,
                    timeRestBestScoreSec,
                    timeRestMediumScoreSec,
                    R.integer.medium_points_max,
                    R.integer.medium_points_medium,
                    R.integer.medium_points_min
                )
            }

            Level.HARD -> {
                GameSettings(
                    allQuestions,
                    timeMaxSec,
                    timeRestBestScoreSec,
                    timeRestMediumScoreSec,
                    R.integer.hard_points_max,
                    R.integer.hard_points_medium,
                    R.integer.hard_points_min
                )
            }
        }
    }


    override fun generateQuestion(level: Level, questionNumber: Int): Question {

        val country: Country
        val maxQuestionNumber = R.integer.test_questions
        val countriesListFull = countriesListFull_LD.value?.toList()
        if (questionNumber <= maxQuestionNumber && countriesListFull != null) {

            if (questionNumber == 1) {
                resetCountriesListNotUsedInQuiz_LD()
            }
            val array = intArrayOf(1, 1, 1, 1)
            listOddQuestions = getOddList(level)
            listEvenQuestions = getEvenList(level)
            val questionId = getQuestionID(questionNumber)
            country =
                countriesListFull.stream().filter { it.id == questionId }.findFirst()
                    .get()
            array[0] = country.id
            var listFullWithoutRightAnswer = countriesListFull.stream().filter {
                it.id != country.id
            }.collect(Collectors.toList())
            for (i in 1..<NUMBER_ANSWER_OPTIONS) {
                val randInt = rand.nextInt(listFullWithoutRightAnswer.size)
                array[i] = randInt
                listFullWithoutRightAnswer = countriesListFull.stream().filter {
                    it.id != randInt
                }.collect(Collectors.toList())
            }
            val rightAnswerPositionInArray = rand.nextInt(NUMBER_ANSWER_OPTIONS)
            if (rightAnswerPositionInArray != 0) {
                val temp = array[0]
                array[0] = array[rightAnswerPositionInArray]
                array[rightAnswerPositionInArray] = temp
            }
            return Question(
                country.countryName,
                array[0],
                array[1],
                array[2],
                array[3],
                array[rightAnswerPositionInArray]
            )
        } else {
            throw RuntimeException("Question number $questionNumber is more then maximal allowed $maxQuestionNumber")
        }

    }


    private fun deleteCountryFromCillections(idQuestion: Int) {
        listEvenQuestions = listEvenQuestions.stream().filter {
            it.id != idQuestion
        }.collect(Collectors.toList())

        listOddQuestions = listOddQuestions.stream().filter {
            it.id != idQuestion
        }.collect(Collectors.toList())
    }


    private fun getQuestionID(questionNumberInQuiz: Int): Int {
        val idQuestion: Int
        val questionNumInList: Int
        if (questionNumberInQuiz % 2 == 0) {
            questionNumInList = rand.nextInt(listEvenQuestions.size)
            idQuestion = listEvenQuestions[questionNumInList].id

        } else {
            questionNumInList = rand.nextInt(listOddQuestions.size)
            idQuestion = listOddQuestions[questionNumInList].id
        }
        deleteCountryFromCillections(idQuestion)
        return idQuestion
    }


    private fun resetCountriesListNotUsedInQuiz_LD() {
        countriesListNotUsedInQuiz_LD.value = countriesListFull_LD.value
    }

    private fun getOddList(level: Level): List<Country> {
        val countriesListFull = countriesListFull_LD.value?.toList()
        if (countriesListFull != null) {

            val listOdd = when (level) {
                Level.EASY -> {
                    countriesListFull.stream()
                        .filter { e -> e.difficultLevel == DIFFICULT_LEVEL_EASY }
                        .collect(Collectors.toList())
                }

                Level.MEDIUM -> {
                    countriesListFull.stream()
                        .filter { e -> e.difficultLevel <= DIFFICULT_LEVEL_MIDDLE }
                        .collect(Collectors.toList())
                }

                Level.HARD -> {
                    countriesListFull
                }
            }

            return listOdd
        } else {
            throw RuntimeException("Empty list in fun: getOddList")
        }
    }

    private fun getEvenList(level: Level): List<Country> {
        val countriesListFull = countriesListFull_LD.value?.toList()
        if (countriesListFull != null) {
            val listEven = when (level) {
                Level.EASY -> {
                    countriesListFull.stream()
                        .filter { e -> e.difficultLevel == DIFFICULT_LEVEL_EASY }
                        .collect(Collectors.toList())
                }

                Level.MEDIUM -> {
                    countriesListFull.stream()
                        .filter { e -> e.difficultLevel == DIFFICULT_LEVEL_MIDDLE }
                        .collect(Collectors.toList())
                }

                Level.HARD -> {
                    countriesListFull.stream()
                        .filter { e -> e.difficultLevel == DIFFICULT_LEVEL_HARD }
                        .collect(Collectors.toList())
                }
            }


            return listEven
        } else {
            throw RuntimeException("Empty list in fun: getEvenList")
        }
    }


}