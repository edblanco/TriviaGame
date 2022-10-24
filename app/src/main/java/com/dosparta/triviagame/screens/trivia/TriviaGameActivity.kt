package com.dosparta.triviagame.screens.trivia

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import androidx.appcompat.app.AppCompatActivity
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.dosparta.triviagame.R
import com.dosparta.triviagame.common.Utils
import com.dosparta.triviagame.networking.VolleySingleton
import com.dosparta.triviagame.networking.schemas.QuestionsSchema
import com.dosparta.triviagame.questions.Answer
import com.dosparta.triviagame.questions.Question
import com.dosparta.triviagame.screens.common.AlertDialogListener
import com.google.android.material.snackbar.Snackbar
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class TriviaGameActivity : AppCompatActivity(), TriviaGameViewMvc.Listener {

    private val tag = TriviaGameActivity::class.java.simpleName

    private val questions: MutableList<Question> = mutableListOf()
    private var currentQuestion: Int = 0
    private var correctAnswers: Int = 0

    private var _viewMvc: TriviaGameViewMvc? = null
    private val viewMvc get() = _viewMvc!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        _viewMvc = TriviaGameViewMvc(LayoutInflater.from(this), null)
        setContentView(viewMvc.getRootView())
        viewMvc.registerListener(this)
    }

    override fun onStart() {
        super.onStart()
        loadQuestions()
    }

    private fun loadQuestions() {
        viewMvc.setLoadingState(true)
        val stringRequest = StringRequest(Request.Method.GET, Utils.TRIVIA_API_URL, { response ->
            onQuestionsReceived(response)
        }, {
            onNetworkCallFailed(it)
        })
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)
    }

    private fun onQuestionsReceived(response: String) {
        Log.i(tag, "Response (first 500 chars): ${response.substring(0, 500)}")
        fillQuestionList(response)
        viewMvc.presentQuestion(currentQuestion, questions)
        viewMvc.setLoadingState(false)
    }

    private fun onNetworkCallFailed(it: VolleyError) {
        Log.i(tag, "Unable to retrieve data: ${it.networkResponse}")
        showErrorDialog(if (it.networkResponse != null) it.networkResponse.statusCode else Utils.INTERNAL_SERVER_ERROR)
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

    private fun fillQuestionList(response: String) {
        val jsonData = Json.decodeFromString<QuestionsSchema>(response)
        questions.addAll(questionsSchemaToQuestions(jsonData))
        Log.i(tag, "onStart: $jsonData")
    }

    private fun questionsSchemaToQuestions(questionsSchema: QuestionsSchema) : List<Question>
    {
        val questionList :MutableList<Question> = mutableListOf()
        for (result in questionsSchema.results) {
            val answers :MutableList<Answer> = mutableListOf(Answer(Html.fromHtml(result.correct_answer, Html.FROM_HTML_MODE_COMPACT).toString(), true))
            for (incorrectAnswer in result.incorrect_answers){
                answers.add(Answer(Html.fromHtml(incorrectAnswer, Html.FROM_HTML_MODE_COMPACT).toString(), false))
            }

            val question = Question(
                question = Html.fromHtml(result.question, Html.FROM_HTML_MODE_COMPACT).toString(),
                difficulty = result.difficulty,
                category = result.category,
                type = result.type,
                answers = answers.shuffled()
            )

            questionList.add(question)
        }
        Log.i(tag, "list of question $questionList")
        return questionList
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
        viewMvc.presentQuestion(currentQuestion, questions)
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
        viewMvc.presentQuestion(currentQuestion, questions)
    }

    override fun onAnswerClicked(isCorrect: Boolean) {
        if (isCorrect){
            ++correctAnswers
        }
        moveToNextQuestionWithDelay()
    }

    override fun onDestroy() {
        viewMvc.removeListener(this)
        super.onDestroy()
    }

    companion object {
        private const val JUMP_TO_NEXT_QUESTION_DELAY = 3000L
    }
}