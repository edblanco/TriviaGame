package com.dosparta.triviagame.screens.trivia

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import com.android.volley.VolleyError
import com.dosparta.triviagame.R
import com.dosparta.triviagame.common.Utils
import com.dosparta.triviagame.questions.FetchTriviaQuestionsUseCase
import com.dosparta.triviagame.questions.Question
import com.dosparta.triviagame.screens.common.AlertDialogListener
import com.dosparta.triviagame.screens.common.BaseActivity
import com.google.android.material.snackbar.Snackbar

class TriviaGameActivity : BaseActivity(), ITriviaGameViewMvc.Listener,
    FetchTriviaQuestionsUseCase.Listener {

    private val tag = TriviaGameActivity::class.java.simpleName

    private var questions: List<Question> = listOf()
    private var currentQuestion: Int = 0
    private var correctAnswers: Int = 0

    private var _fetchTriviaQuestionsUseCase: FetchTriviaQuestionsUseCase? = null
    private val fetchTriviaQuestionsUseCase get() = _fetchTriviaQuestionsUseCase!!

    private var _viewMvc: ITriviaGameViewMvc? = null
    private val viewMvc get() = _viewMvc!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _viewMvc = getCompositionRoot().getViewMvcFactory().getTriviaGameViewMvc(null)
        _fetchTriviaQuestionsUseCase = getCompositionRoot().getFetchTriviaQuestionsUseCase(this)

        setContentView(viewMvc.getRootView())
    }

    override fun onStart() {
        super.onStart()
        viewMvc.registerListener(this)
        fetchTriviaQuestionsUseCase.registerListener(this)
        fetchTriviaQuestionsUseCase.fetchTriviaQuestionsAndNotify()
    }

    override fun onStop() {
        super.onStop()
        viewMvc.unregisterListener(this)
        fetchTriviaQuestionsUseCase.unregisterListener(this)
    }

    override fun onTriviaQuestionsFetched(questions: List<Question>) {
        this.questions = questions
        viewMvc.bindQuestions(currentQuestion, questions)
        viewMvc.setLoadingState(false)
    }

    override fun onTriviaQuestionsFetchFailed(error: VolleyError?) {
        Log.i(tag, "Unable to retrieve data: ${error?.networkResponse}")
        showErrorDialog(if (error?.networkResponse != null) error.networkResponse.statusCode else Utils.INTERNAL_SERVER_ERROR)
    }

    private fun showErrorDialog(statusCode: Int) {
        val answerListener = object: AlertDialogListener {
            override fun onPositiveAnswer() {
                startActivity(Intent(this@TriviaGameActivity, TriviaGameActivity::class.java))
                finish()
            }

            override fun onNegativeAnswer() {
                resetGame()
                finishAffinity()
            }
        }
        viewMvc.showErrorDialog(statusCode, answerListener)
    }

    private fun moveToNextQuestionWithDelay() {
        val handler = Handler(mainLooper)
        handler.postDelayed({
            moveToNextQuestion()
        }, JUMP_TO_NEXT_QUESTION_DELAY)
    }

    private fun moveToNextQuestion(){
        if (currentQuestion == (questions.size - 1)){
            Snackbar.make(window.decorView.rootView, getString(R.string.game_over_overlay, correctAnswers, questions.size)
                , Snackbar.LENGTH_SHORT).show()
            showResults()
            return
        }
        ++currentQuestion
        viewMvc.bindQuestions(currentQuestion, questions)
    }

    private fun showResults() {
        val answerListener = object: AlertDialogListener {
            override fun onPositiveAnswer() {
                startActivity(Intent(this@TriviaGameActivity, TriviaGameActivity::class.java))
                finish()
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
        viewMvc.bindQuestions(currentQuestion, questions)
    }

    override fun onAnswerClicked(isCorrect: Boolean) {
        if (isCorrect){
            ++correctAnswers
        }
        moveToNextQuestionWithDelay()
    }

    companion object {
        private const val JUMP_TO_NEXT_QUESTION_DELAY = 3000L
    }
}