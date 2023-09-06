package com.example.geoquiz

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

private const val TAG = "QuizViewModel"
const val CURRENT_INDEX_KEY = "CURRENT_INDEX_KEY"
const val IS_CHEATER_KEY = "IS_CHEATER_KEY"

class QuizViewModel(private val savedStateHandle: SavedStateHandle): ViewModel() {

    private val questionBank = listOf(
        Question(R.string.question_computer_history, true, false, false),
        Question(R.string.question_java_history, false, false, false),
        Question(R.string.question_python_return, true, false, false),
        Question(R.string.question_javascript_design, true, false, false),
        Question(R.string.question_c_interpreter, false, false, false),
        Question(R.string.question_java_popularity, true, false, false),
        Question(R.string.question_python_poem, false, false, false),
        Question(R.string.question_javascript_coding, false, false, false),
        Question(R.string.question_c_level, true, false, false)
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

    val isAnswered: Boolean
        get() = questionBank[currentIndex].answered

    fun moveToNext() {
        currentIndex = (currentIndex + 1) % questionBank.size
    }

    fun moveToPrev() {
        currentIndex = if ((currentIndex - 1) >= 0) {
            currentIndex - 1
        } else {
            questionBank.size - 1
        }
    }

    fun wasAnswered(buttonPressed: Boolean) {
        questionBank[currentIndex].answered = buttonPressed
    }

    fun setCorrect(isCorrect: Boolean) {
        questionBank[currentIndex].correct = isCorrect
    }

    fun checkFinished(): Boolean {
        for (question in questionBank) {
            if (!question.answered) {
                return false
            }
        }
        return true
    }

    fun calcGrade(): String {
        var finalGrade = 0.0

        for (question in questionBank) {
            if (question.correct) {
                finalGrade += 1.0
            }
        }

        finalGrade /= questionBank.size
        finalGrade *= 100

        return String.format("%.2f", finalGrade)
    }

}