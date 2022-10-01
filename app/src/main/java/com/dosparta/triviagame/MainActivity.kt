package com.dosparta.triviagame

import android.content.Context
import android.content.Intent
import android.net.ConnectivityManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Html
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.cardview.widget.CardView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.StringRequest
import com.android.volley.toolbox.Volley
import com.google.android.material.snackbar.Snackbar
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json
import java.net.InetAddress
import java.net.URLDecoder.decode
import java.net.URLEncoder

class MainActivity : AppCompatActivity(), OnCorrectAnswerListener {

    val questions: MutableList<Question> = mutableListOf()
    var parentLayout: ConstraintLayout? = null
    var questionTitleTv: TextView? = null
    var questionTv: TextView? = null
    var loadingBar: ProgressBar? = null
    var answersRecyclerView: RecyclerView? = null
    var answersRecyclerAdapter: AnswersRecyclerAdapter? = null
    var currentQuestion: Int = 0
    var correctAnswers: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        parentLayout = findViewById(R.id.parent_layout)
        questionTitleTv = findViewById(R.id.text_view_out_of)
        questionTv = findViewById(R.id.question_textview)
        loadingBar = findViewById(R.id.loading_bar)
        answersRecyclerView = findViewById(R.id.answers_recycler_view)
        answersRecyclerView?.layoutManager = LinearLayoutManager(this)
    }

    override fun onStart() {
        super.onStart()
        loadingBar?.visibility = View.VISIBLE
        parentLayout?.alpha = 0.5f

        val url = "https://opentdb.com/api.php?amount=10"
        val stringRequest = StringRequest(Request.Method.GET, url, { response ->
            println("Response is: ${response.substring(0, 500)}")
            //todo -> fill Questions
            val jsonData = Json.decodeFromString<QuestionsSchema>(response)
            questions.addAll(questionsSchemaToQuestions(jsonData))
            questionTitleTv?.text = getString(R.string.out_of_text_placeholders, currentQuestion + 1, questions.size)
            questionTv?.text = questions[currentQuestion].question
            println(jsonData)
            // todo --> present question on the screen

            initAdapter(currentQuestion)
            loadingBar?.visibility = View.GONE
            parentLayout?.alpha = 1f
        }, {
            println("Unable to retrieve data: ${it.networkResponse}")
            val builder = AlertDialog.Builder(this)
            val statusCode = if (it.networkResponse != null) it.networkResponse.statusCode else ""
            builder.setTitle("Network error! $statusCode")
                .setMessage("A network error ocurred." +
                        "\nDo you want to retry or close the App")
                .setPositiveButton("Retry") {dialog, _ ->
                    dialog.dismiss()
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                .setNegativeButton("Close App"){dialog, _ ->
                    dialog.dismiss()
                    finishAffinity()
                }
                .setCancelable(false)
                .show()
        })
        VolleySingleton.getInstance(this).addToRequestQueue(stringRequest)
    }

    private fun initAdapter(questionPos: Int) {
        answersRecyclerAdapter = AnswersRecyclerAdapter(questions[questionPos].answers, this)
        answersRecyclerView?.adapter = answersRecyclerAdapter
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
        println("list of question $questionList")
        return questionList
    }

    override fun onCorrect(isCorrect: Boolean) {
        Snackbar.make(window.decorView.rootView, "Your answers was $isCorrect", Snackbar.LENGTH_SHORT).show()
        if (isCorrect){
            ++correctAnswers
        }
        val handler = Handler(mainLooper)
        handler.postDelayed({
            moveToNextQuestion()
        }, 3000)
    }

    private fun moveToNextQuestion(){
        if (currentQuestion == (questions.size - 1)){
            Snackbar.make(window.decorView.rootView, "Game is over. Correct answer $correctAnswers/${questions.size}", Snackbar.LENGTH_SHORT).show()
            //todo show dialog with restart game with same questions or with new questions
            showResults()
            return
        }
        currentQuestion++
        questionTv?.text = questions[currentQuestion].question
        questionTitleTv?.text = getString(R.string.out_of_text_placeholders, currentQuestion + 1, questions.size)
        initAdapter(currentQuestion)
    }

    private fun showResults() {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Game over!")
            .setMessage("Correct answers $correctAnswers/${questions.size}" +
                    "\nScore ${(correctAnswers * 100) / questions.size}%" +
                    "\nWould you like to start a new game or repeat this game (same questions)?")
            .setPositiveButton("New") {dialog, _ ->
                dialog.dismiss()
                startActivity(Intent(this, MainActivity::class.java))
                finish()
            }
            .setNegativeButton("Repeat"){dialog, _ ->
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
}