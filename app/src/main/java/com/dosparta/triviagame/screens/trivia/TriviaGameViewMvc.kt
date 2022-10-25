package com.dosparta.triviagame.screens.trivia

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dosparta.triviagame.R
import com.dosparta.triviagame.questions.Question
import com.dosparta.triviagame.screens.common.AlertDialogListener
import com.google.android.material.snackbar.Snackbar
import com.dosparta.triviagame.screens.trivia.ITriviaGameViewMvc.Listener

class TriviaGameViewMvc(inflater: LayoutInflater, parent: ViewGroup?) : OnCorrectAnswerListener,
    ITriviaGameViewMvc {

    private var rootView: View?

    private val listeners: MutableList<Listener> = mutableListOf()

    private var answersRecyclerAdapter: AnswersRecyclerAdapter? = null
    private var parentLayout: ConstraintLayout? = null
    private var questionTitleTv: TextView? = null
    private var questionTv: TextView? = null
    private var loadingBar: ProgressBar? = null
    private var answersRecyclerView: RecyclerView? = null

    init {
        rootView = inflater.inflate(R.layout.activity_main, parent, false)
        attachUI()
    }

    private fun attachUI() {
        parentLayout = findViewById(R.id.parent_layout)
        questionTitleTv = findViewById(R.id.text_view_out_of)
        questionTv = findViewById(R.id.question_textview)
        loadingBar = findViewById(R.id.loading_bar)
        answersRecyclerView = findViewById(R.id.answers_recycler_view)
        answersRecyclerView?.layoutManager = LinearLayoutManager(getContext())
    }

    private fun <T: View> findViewById(id: Int) : T?{
        return getRootView().findViewById(id)
    }

    override fun getRootView(): View {
        return rootView!!
    }

    override fun registerListener(listener: Listener){
        listeners.add(listener)
    }

    override fun unregisterListener(listener: Listener){
        listeners.remove(listener)
    }

    private fun getContext(): Context{
        return getRootView().context
    }

    override fun setLoadingState(loading: Boolean) {
        loadingBar?.visibility = if (loading) View.VISIBLE else View.GONE
        parentLayout?.alpha = if (loading) ALPHA_FIFTY_PERCENT else ALPHA_HUNDRED_PERCENT
    }

    override fun bindQuestions(currentQuestion: Int, questions: List<Question>) {
        questionTitleTv?.text = getContext().getString(R.string.out_of_text_placeholders, currentQuestion + 1, questions.size)
        questionTv?.text = questions[currentQuestion].question
        initAdapter(currentQuestion, questions)
    }

    private fun initAdapter(questionPos: Int, questions: List<Question>) {
        answersRecyclerAdapter = AnswersRecyclerAdapter(questions[questionPos].answers, this)
        answersRecyclerView?.adapter = answersRecyclerAdapter
    }

    override fun showResults(correctAnswers: Int, totalAnswers: Int, answerListener: AlertDialogListener) {
        val builder = AlertDialog.Builder(getContext())
        val correctAnswersOverTotal = (correctAnswers * HUNDRED_PERCENT) / totalAnswers
        builder.setTitle(R.string.game_over_popup_title)
            .setMessage(getContext().getString(R.string.game_over_popup_msg, correctAnswers, totalAnswers, correctAnswersOverTotal))
            .setPositiveButton(R.string.game_over_popup_positive_msg) { dialog, _ ->
                dialog.dismiss()
                answerListener.onPositiveAnswer()
            }
            .setNegativeButton(R.string.game_over_popup_negative_msg){ dialog, _ ->
                dialog.dismiss()
                answerListener.onNegativeAnswer()
            }
            .setCancelable(false)
            .show()
    }

    override fun showErrorDialog(statusCode: Int, answerListener: AlertDialogListener) {
        val builder = AlertDialog.Builder(getContext())
        builder.setTitle(getContext().getString(R.string.network_error_popup_title, statusCode))
            .setMessage(R.string.network_error_popup_msg)
            .setPositiveButton(R.string.network_error_popup_positive_msg) { dialog, _ ->
                dialog.dismiss()
                answerListener.onPositiveAnswer()
            }
            .setNegativeButton(R.string.network_error_popup_negative_msg) { dialog, _ ->
                dialog.dismiss()
                answerListener.onNegativeAnswer()
            }
            .setCancelable(false)
            .show()
    }

    override fun onCorrect(isCorrect: Boolean) {
        Snackbar.make(getRootView(), getContext().getString(R.string.right_question_overlay, isCorrect), Snackbar.LENGTH_SHORT).show()
        for (listener in listeners){
            listener.onAnswerClicked(isCorrect)
        }
    }

    companion object {
        private const val HUNDRED_PERCENT = 100
        private const val ALPHA_HUNDRED_PERCENT = 1.0f
        private const val ALPHA_FIFTY_PERCENT = 0.5f
    }
}