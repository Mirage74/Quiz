package com.balex.quiz.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.balex.quiz.data.api.ApiFactory
import com.balex.quiz.domain.entity.Country
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
    private val rand = java.util.Random()


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

    override fun generateQuestion(level: Level, questionNumber: Int): Question {
        if (questionNumber == 1) {
            resetCountriesListNotUsedInQuiz_LD()
        }
        val array = arrayOfNulls<Int>(NUMBER_ANSWER_OPTIONS)
        val rightAnswerPositionInArray = rand.nextInt(NUMBER_ANSWER_OPTIONS)
        listOddQuestions = getOddList(level)
        listEvenQuestions = getEvenList(level)
        val questionId = getQuestionID(questionNumber)


    }

    fun deleteCountryFromCillections(idQuestion: Int) {
        listEvenQuestions = listOddQuestions.stream().filter {
            it.id != idQuestion
        }.collect(Collectors.toList())

        listOddQuestions = listOddQuestions.stream().filter {
            it.id != idQuestion
        }.collect(Collectors.toList())
    }


    fun getQuestionID(questionNumberInQuiz: Int): Int {
        var idQuestion: Int
        var questionNumInList: Int
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



fun resetCountriesListNotUsedInQuiz_LD() {
    countriesListNotUsedInQuiz_LD.value = countriesListFull_LD.value
}

fun getOddList(level: Level): List<Country> {
    val listOdd = when (level) {
        Level.EASY -> {
            countriesListFull_LD.value?.stream()
                ?.filter { e -> e.difficultLevel <= DIFFICULT_LEVEL_EASY }
                ?.collect(Collectors.toList());
        }

        Level.MEDIUM -> {
            countriesListFull_LD.value?.stream()
                ?.filter { e -> e.difficultLevel <= DIFFICULT_LEVEL_MIDDLE }
                ?.collect(Collectors.toList());
        }

        Level.HARD -> {
            countriesListFull_LD.value
        }
    }
    var list: List<Country> = Collections.emptyList()
    listOdd?.let { list = listOdd }

    return list
}

fun getEvenList(level: Level): List<Country> {
    val listEven = when (level) {
        Level.EASY -> {
            countriesListFull_LD.value?.stream()
                ?.filter { e -> e.difficultLevel == DIFFICULT_LEVEL_EASY }
                ?.collect(Collectors.toList());
        }

        Level.MEDIUM -> {
            countriesListFull_LD.value?.stream()
                ?.filter { e -> e.difficultLevel == DIFFICULT_LEVEL_MIDDLE }
                ?.collect(Collectors.toList());
        }

        Level.HARD -> {
            countriesListFull_LD.value?.stream()
                ?.filter { e -> e.difficultLevel == DIFFICULT_LEVEL_HARD }
                ?.collect(Collectors.toList());
        }
    }

    var list: List<Country> = Collections.emptyList()
    listEven?.let { list = listEven }

    return list

}


}