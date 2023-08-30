package com.example.geoquiz

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"

class QuizViewModel(private val savedStateHandle: SavedStateHandle): ViewModel() {

    private val questionBank = listOf(
        Question(R.string.question_computer_history, true),
        Question(R.string.question_java_history, false),
        Question(R.string.question_python_return, true),
        Question(R.string.question_javascript_design, true),
        Question(R.string.question_c_interpreter, false),
        Question(R.string.question_java_popularity, true),
        Question(R.string.question_python_poem, false),
        Question(R.string.question_javascript_coding, false),
        Question(R.string.question_c_level, true)
    )

    var isCheater: Boolean
        get() = savedStateHandle.get(IS_CHEATER_KEY) ?: false
        set(value) = savedStateHandle.set(IS_CHEATER_KEY, value)

    private var currentIndex: Int
        get() = savedStateHandle.get(CURRENT_INDEX_KEY) ?: 0
        set(value) = savedStateHandle.set(CURRENT_INDEX_KEY, value)

    val currentQuestionAnswer: Boolean
        get() = questionBank[currentIndex].answer

    val currentQuestionText: Int
        get() = questionBank[currentIndex].textResId

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        currentIndex = if ((currentIndex - 1) >= 0) {
            currentIndex - 1
        } else {
            0
        }
    }

}