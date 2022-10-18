package com.dosparta.triviagame

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.util.Log
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.VolleyError
import com.android.volley.toolbox.StringRequest
import com.google.android.material.snackbar.Snackbar
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class MainActivity : AppCompatActivity(), OnCorrectAnswerListener {

    private val tag = MainActivity::class.java.simpleName

    private val questions: MutableList<Question> = mutableListOf()
    private var parentLayout: ConstraintLayout? = null
    private var questionTitleTv: TextView? = null
    private var questionTv: TextView? = null
    private var loadingBar: ProgressBar? = null
    private var answersRecyclerView: RecyclerView? = null
    private var answersRecyclerAdapter: AnswersRecyclerAdapter? = null
    private var currentQuestion: Int = 0
    private var correctAnswers: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        attachUI()
    }

    private fun attachUI() {
        parentLayout = findViewById(R.id.parent_layout)
        questionTitleTv = findViewById(R.id.text_view_out_of)
        questionTv = findViewById(R.id.question_textview)
        loadingBar = findViewById(R.id.loading_bar)
        answersRecyclerView = findViewById(R.id.answers_recycler_view)
        answersRecyclerView?.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        loadQuestions()
    }

    private fun loadQuestions() {
        setLoadingState(true)
        val stringRequest = StringRequest(Request.Method.GET, Utils.TRIVIA_API_URL, { response ->
            onQuestionsReceived(response)
        }, {
            onNetworkCallFailed(it)
        })
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)
    }

    private fun setLoadingState(loading: Boolean) {
        loadingBar?.visibility = if (loading) View.VISIBLE else View.GONE
        parentLayout?.alpha = if (loading) ALPHA_FIFTY_PERCENT else ALPHA_HUNDRED_PERCENT
    }

    private fun onQuestionsReceived(response: String) {
        Log.i(tag, "Response (first 500 chars): ${response.substring(0, 500)}")
        fillQuestionList(response)
        presentQuestion()
        setLoadingState(false)
    }

    private fun onNetworkCallFailed(it: VolleyError) {
        Log.i(tag, "Unable to retrieve data: ${it.networkResponse}")
        showErrorDialog(if (it.networkResponse != null) it.networkResponse.statusCode else Utils.INTERNAL_SERVER_ERROR)
    }

    private fun showErrorDialog(statusCode: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle(getString(R.string.network_error_popup_title, statusCode))
            .setMessage(R.string.network_error_popup_msg)
            .setPositiveButton(R.string.network_error_popup_positive_msg) { dialog, _ ->
                dialog.dismiss()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .setNegativeButton(R.string.network_error_popup_negative_msg) { dialog, _ ->
                dialog.dismiss()
                finishAffinity()
            }
            .setCancelable(false)
            .show()
    }

    private fun fillQuestionList(response: String) {
        val jsonData = Json.decodeFromString<QuestionsSchema>(response)
        questions.addAll(questionsSchemaToQuestions(jsonData))
        Log.i(tag, "onStart: $jsonData")
    }

    private fun presentQuestion() {
        questionTitleTv?.text = getString(R.string.out_of_text_placeholders, currentQuestion + 1, questions.size)
        questionTv?.text = questions[currentQuestion].question
        initAdapter(currentQuestion)
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

    private fun initAdapter(questionPos: Int) {
        answersRecyclerAdapter = AnswersRecyclerAdapter(questions[questionPos].answers, this)
        answersRecyclerView?.adapter = answersRecyclerAdapter
    }

    override fun onCorrect(isCorrect: Boolean) {
        Snackbar.make(window.decorView.rootView, getString(R.string.right_question_overlay, isCorrect), Snackbar.LENGTH_SHORT).show()
        if (isCorrect){
            ++correctAnswers
        }
        moveToNextQuestionWithDelay()
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
        presentQuestion()
    }

    private fun showResults() {
        val builder = AlertDialog.Builder(this)
        val correctAnswersOverTotal = (correctAnswers * HUNDRED_PERCENT) / questions.size
        builder.setTitle(R.string.game_over_popup_title)
            .setMessage(getString(R.string.game_over_popup_msg, correctAnswers, questions.size, correctAnswersOverTotal))
            .setPositiveButton(R.string.game_over_popup_positive_msg) {dialog, _ ->
                dialog.dismiss()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .setNegativeButton(R.string.game_over_popup_negative_msg){dialog, _ ->
                dialog.dismiss()
                resetGame()
            }
            .setCancelable(false)
            .show()
    }

    private fun resetGame() {
        correctAnswers = 0
        currentQuestion = 0
        questionTitleTv?.text = getString(R.string.out_of_text_placeholders, currentQuestion + 1, questions.size)
        questionTv?.text = questions[currentQuestion].question
        initAdapter(currentQuestion)
    }

    companion object {
        private const val JUMP_TO_NEXT_QUESTION_DELAY = 3000L
        private const val HUNDRED_PERCENT = 100
        private const val ALPHA_HUNDRED_PERCENT = 1.0f
        private const val ALPHA_FIFTY_PERCENT = 0.5f
    }
}