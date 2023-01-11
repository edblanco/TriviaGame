package com.dosparta.triviagame.screens.trivia

import android.os.Bundle
import android.util.Log
import com.android.volley.VolleyError
import com.dosparta.triviagame.common.Utils
import com.dosparta.triviagame.questions.Answer
import com.dosparta.triviagame.questions.FetchTriviaQuestionsUseCase
import com.dosparta.triviagame.questions.Question
import com.dosparta.triviagame.screens.common.ActivityUtils
import com.dosparta.triviagame.screens.common.popups.AlertDialogListener
import com.dosparta.triviagame.screens.common.popups.OverlayMessagesHelper
import com.dosparta.triviagame.screens.common.screensnavigator.ScreensNavigator
import com.dosparta.triviagame.screens.trivia.answersitem.IAnswersItemViewMvc

class TriviaGameController(
    private val fetchTriviaQuestionsUseCase: FetchTriviaQuestionsUseCase,
    private val screensNavigator: ScreensNavigator,
    private val overlayMessagesHelper: OverlayMessagesHelper,
    private val activityUtils: ActivityUtils
) : ITriviaGameController, ITriviaGameViewMvc.Listener, FetchTriviaQuestionsUseCase.Listener {

    private var questions: List<Question> = listOf()
    private var currentQuestion: Int = 0
    private var correctAnswers: Int = 0
    private var isGameOver = false

    private var _viewMvc: ITriviaGameViewMvc? = null
    private val viewMvc get() = _viewMvc!!

    override fun onStart() {
        viewMvc.registerListener(this)
        fetchTriviaQuestionsUseCase.registerListener(this)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        currentQuestion = savedInstanceState.getInt(CURRENT_QUESTION)
        correctAnswers = savedInstanceState.getInt(CORRECT_ANSWERS)
        isGameOver = savedInstanceState.getBoolean(GAME_OVER)
        questions =
            savedInstanceState.getParcelableArrayList<Question>(QUESTIONS)?.toList() ?: questions
    }

    override fun onResume() {
        if (questions.isEmpty()) {
            fetchTriviaQuestionsUseCase.fetchTriviaQuestionsAndNotify()
        } else {
            onTriviaQuestionsFetched(questions)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CURRENT_QUESTION, currentQuestion)
        outState.putInt(CORRECT_ANSWERS, correctAnswers)
        outState.putBoolean(GAME_OVER, isGameOver)
        outState.putParcelableArrayList(QUESTIONS, ArrayList(questions))
    }

    override fun onStop() {
        viewMvc.unregisterListener(this)
        fetchTriviaQuestionsUseCase.unregisterListener(this)
    }

    override fun onTriviaQuestionsFetched(questions: List<Question>) {
        this.questions = questions
        viewMvc.bindQuestions(currentQuestion, questions)
        viewMvc.setLoadingState(false)
        if (isGameOver) {
            showResults()
        }
    }

    override fun onTriviaQuestionsFetchFailed(error: VolleyError?) {
        Log.i(tag, "Unable to retrieve data: ${error?.networkResponse}")
        showErrorDialog(if (error?.networkResponse != null) error.networkResponse.statusCode else Utils.INTERNAL_SERVER_ERROR)
    }

    override fun bindView(viewMvc: ITriviaGameViewMvc) {
        _viewMvc = viewMvc
    }

    private fun showErrorDialog(statusCode: Int) {
        val answerListener = object : AlertDialogListener {
            override fun onPositiveAnswer() {
                screensNavigator.toTriviaGame()
                screensNavigator.closeScreen()
            }

            override fun onNegativeAnswer() {
                screensNavigator.closeApp()
            }
        }
        viewMvc.showErrorDialog(statusCode, answerListener)
    }

    override fun onAnswerClicked(answer: Answer, answersViewMvc: IAnswersItemViewMvc) {
        val isCorrect = answer.correct
        answersViewMvc.updateTintColor(answer.correct)

        if (isCorrect) {
            ++correctAnswers
        } else {
            viewMvc.updateCorrectQuestion()
        }

        viewMvc.showButtonNext(true)
        moveToNextQuestionWithDelay()
    }

    private fun moveToNextQuestionWithDelay() {
        activityUtils.postDelayed({
            moveToNextQuestion()
        }, JUMP_TO_NEXT_QUESTION_DELAY)
    }

    override fun onButtonNextClicked() {
        activityUtils.removeCallbacksAndMessages(null)
        moveToNextQuestion()
    }

    override fun onCorrectAnswerFound(answersItemViewMvc: IAnswersItemViewMvc) {
        answersItemViewMvc.updateTintColor(true)
    }

    private fun moveToNextQuestion() {
        if (currentQuestion == (questions.size - 1)) {
            overlayMessagesHelper.showGameOverOverlay(correctAnswers, questions.size)
            isGameOver = true
            showResults()
            return
        }
        ++currentQuestion
        viewMvc.showButtonNext(false)
        viewMvc.bindQuestions(currentQuestion, questions)
    }

    private fun showResults() {
        val answerListener = object : AlertDialogListener {
            override fun onPositiveAnswer() {
                screensNavigator.toTriviaGame()
                screensNavigator.closeScreen()
            }

            override fun onNegativeAnswer() {
                resetGame()
            }
        }
        viewMvc.showResults(correctAnswers, questions.size, answerListener)
    }

    private fun resetGame() {
        correctAnswers = 0
        currentQuestion = 0
        isGameOver = false
        viewMvc.bindQuestions(currentQuestion, questions)
    }

    companion object {
        private val tag = TriviaGameActivity::class.java.simpleName
        private const val JUMP_TO_NEXT_QUESTION_DELAY = 3000L
        private const val CURRENT_QUESTION = "CURRENT_QUESTION"
        private const val CORRECT_ANSWERS = "CORRECT_ANSWERS"
        private const val QUESTIONS = "QUESTIONS"
        private const val GAME_OVER = "GAME_OVER"
    }
}