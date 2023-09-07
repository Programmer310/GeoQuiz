package com.example.geoquiz

import android.app.Activity
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.example.geoquiz.databinding.ActivityMainBinding

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private val quizViewModel: QuizViewModel by viewModels()

    private val cheatLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        // Handle the result
        if (result.resultCode == Activity.RESULT_OK) {
            quizViewModel.isCheater =
                result.data?.getBooleanExtra(EXTRA_ANSWER_SHOWN, false) ?: false
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate(Bundle?) called")
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "Got a QuizViewModel: $quizViewModel")

        binding.trueButton.setOnClickListener { view: View ->
            quizViewModel.wasAnswered(true)
            setButtonState()
            checkAnswer(true)
        }

        binding.falseButton.setOnClickListener { view: View ->
            quizViewModel.wasAnswered(true)
            setButtonState()
            checkAnswer(false)
        }

        binding.nextButton.setOnClickListener { view: View ->
            quizViewModel.moveToNext()
            updateQuestion()
        }

        binding.previousButton.setOnClickListener { view: View ->
            quizViewModel.moveToPrev()
            updateQuestion()
        }

        binding.cheatButton.setOnClickListener { view: View ->
            // Start CheatActivity
            val answerIsTrue = quizViewModel.currentQuestionAnswer
            val intent = CheatActivity.newIntent(this@MainActivity, answerIsTrue)
            cheatLauncher.launch(intent)
        }

        updateQuestion()

    }

    override fun onStart() {
        super.onStart()
        Log.d(TAG, "onStart() called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause() called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop() called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy() called")
    }

    private fun updateQuestion() {
        val questionTextResId = quizViewModel.currentQuestionText
        binding.questionTextView.setText(questionTextResId)
        setButtonState()
    }

    private fun checkAnswer(userAnswer: Boolean) {
        if (quizViewModel.checkFinished()) {
            val gradeString = "Grade: ${quizViewModel.calcGrade()}%\nPlease restart app to play again."
            Toast.makeText(this, gradeString, Toast.LENGTH_SHORT).show()
        } else {
            val correctAnswer = quizViewModel.currentQuestionAnswer

            val messageResId = when {
                quizViewModel.isCheater -> R.string.judgement_toast
                userAnswer == correctAnswer -> {
                    quizViewModel.setCorrect(true)
                    R.string.correct_toast
                }
                else -> R.string.incorrect_toast
            }

            Toast.makeText(this, messageResId, Toast.LENGTH_SHORT).show()
        }
    }

    private fun setButtonState() {
        val answerButtonState = when {
            quizViewModel.isAnswered -> {
                binding.trueButton.setBackgroundColor(
                    ContextCompat.getColor(this, R.color.gray))
                binding.falseButton.setBackgroundColor(
                    ContextCompat.getColor(this, R.color.gray))
                binding.cheatButton.setBackgroundColor(
                    ContextCompat.getColor(this, R.color.gray))
                false
            }
            else -> {
                binding.trueButton.setBackgroundColor(
                    ContextCompat.getColor(this, R.color.green))
                binding.falseButton.setBackgroundColor(
                    ContextCompat.getColor(this, R.color.red))
                binding.cheatButton.setBackgroundColor(
                    ContextCompat.getColor(this, R.color.purple))
                true
            }
        }

        binding.trueButton.isEnabled = answerButtonState
        binding.falseButton.isEnabled = answerButtonState
        binding.cheatButton.isEnabled = answerButtonState
    }
}