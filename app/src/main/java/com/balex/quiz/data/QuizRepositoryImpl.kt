package com.balex.quiz.data

import android.app.Application
import androidx.lifecycle.MutableLiveData
import com.balex.quiz.R
import com.balex.quiz.data.api.ApiFactory
import com.balex.quiz.domain.entity.Country
import com.balex.quiz.domain.entity.GameSettings
import com.balex.quiz.domain.entity.Level
import com.balex.quiz.domain.entity.Question
import com.balex.quiz.domain.repository.QuizRepository
import com.bumptech.glide.Glide
import java.util.Collections
import java.util.Random
import java.util.stream.Collectors


class QuizRepositoryImpl(private val application: Application) : QuizRepository {

    val BACKEND_STATIC_IMAGES_PREFIX = "images"

    private val countriesListFull_LD = MutableLiveData<List<Country>>()

    private var countriesListNotUsedInQuiz_LD: List<Country> = Collections.emptyList()
    private var listOddQuestions: List<Country> = Collections.emptyList()
    private var listEvenQuestions: List<Country> = Collections.emptyList()


    private val NUMBER_ANSWER_OPTIONS = 4
    private val DIFFICULT_LEVEL_EASY = 0
    private val DIFFICULT_LEVEL_MIDDLE = 1
    private val DIFFICULT_LEVEL_HARD = 2
    private val rand = Random()


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

    override fun generateQuestion(
        level: Level,
        questionNumber: Int,
        countriesListFull: List<Country>,
        countriesListNotUsedInQuiz: List<Country>
    ): Question {

        val country: Country
        val maxQuestionNumber = R.integer.test_questions
        if (questionNumber <= maxQuestionNumber) {
            val array = intArrayOf(1, 1, 1, 1)
            listOddQuestions = getOddList(level, countriesListNotUsedInQuiz)
            listEvenQuestions = getEvenList(level, countriesListNotUsedInQuiz)
            val questionId = generateQuestionID(questionNumber)
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


            val question = Question(
                country.countryName,
                array[0],
                array[1],
                array[2],
                array[3],
                array[rightAnswerPositionInArray]
            )

            Glide.with(application)
                .load(ApiFactory.apiService.loadCountryImage(BACKEND_STATIC_IMAGES_PREFIX + "/" + country.imageName))
                .into(question.imageView)

            return question
        } else {
            throw RuntimeException("Question number $questionNumber is more then maximal allowed $maxQuestionNumber")
        }

    }


    private fun deleteCountryFromCollections(idQuestion: Int) {
        listEvenQuestions = listEvenQuestions.stream().filter {
            it.id != idQuestion
        }.collect(Collectors.toList())

        listOddQuestions = listOddQuestions.stream().filter {
            it.id != idQuestion
        }.collect(Collectors.toList())

        countriesListNotUsedInQuiz_LD = countriesListNotUsedInQuiz_LD.stream().filter {
            it.id != idQuestion
        }.collect(Collectors.toList())
    }


    private fun generateQuestionID(questionNumberInQuiz: Int): Int {
        val idQuestion: Int
        val questionNumInList: Int
        if (questionNumberInQuiz % 2 == 0) {
            questionNumInList = rand.nextInt(listEvenQuestions.size)
            idQuestion = listEvenQuestions[questionNumInList].id

        } else {
            questionNumInList = rand.nextInt(listOddQuestions.size)
            idQuestion = listOddQuestions[questionNumInList].id
        }
        deleteCountryFromCollections(idQuestion)
        return idQuestion
    }


    private fun getOddList(level: Level, countriesListFull: List<Country>): List<Country> {

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
    }

    private fun getEvenList(level: Level, countriesListFull: List<Country>): List<Country> {

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
    }


}