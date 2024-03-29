package com.dosparta.triviagame.screens.trivia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.dosparta.triviagame.R
import com.dosparta.triviagame.questions.Answer
import com.dosparta.triviagame.questions.Question
import com.dosparta.triviagame.screens.common.ViewMvcFactory
import com.dosparta.triviagame.screens.common.popups.AlertDialogListener
import com.dosparta.triviagame.screens.common.views.BaseObservableViewMvc
import com.dosparta.triviagame.screens.trivia.answersitem.AnswersRecyclerAdapter
import com.dosparta.triviagame.screens.trivia.answersitem.IAnswersItemViewMvc
import com.dosparta.triviagame.screens.trivia.answersitem.OnCorrectAnswerListener

class TriviaGameViewMvc(
    inflater: LayoutInflater,
    parent: ViewGroup?,
    private val viewMvcFactory: ViewMvcFactory
) : BaseObservableViewMvc<ITriviaGameViewMvc.Listener>(), OnCorrectAnswerListener,
    ITriviaGameViewMvc {

    private lateinit var answersRecyclerAdapter: AnswersRecyclerAdapter
    private lateinit var parentLayout: ConstraintLayout
    private lateinit var questionTitleTv: TextView
    private lateinit var questionTv: TextView
    private lateinit var buttonNext: Button
    private lateinit var loadingBar: ProgressBar
    private lateinit var answersRecyclerView: RecyclerView

    init {
        setRootView(inflater.inflate(R.layout.activity_main, parent, false))
        attachUI()
        registerClickListeners()
    }

    private fun attachUI() {
        parentLayout = findViewById(R.id.parent_layout)
        questionTitleTv = findViewById(R.id.text_view_out_of)
        questionTv = findViewById(R.id.question_textview)
        buttonNext = findViewById(R.id.button_next)
        loadingBar = findViewById(R.id.loading_bar)
        answersRecyclerView = findViewById(R.id.answers_recycler_view)
        answersRecyclerView.layoutManager = LinearLayoutManager(getContext())
    }

    private fun registerClickListeners() {
        buttonNext.setOnClickListener {
            for (listener in getListeners()){
                listener.onButtonNextClicked()
            }
        }
    }

    override fun setLoadingState(loading: Boolean) {
        loadingBar.visibility = if (loading) View.VISIBLE else View.GONE
        parentLayout.alpha = if (loading) ALPHA_FIFTY_PERCENT else ALPHA_HUNDRED_PERCENT
    }

    override fun bindQuestions(currentQuestion: Int, questions: List<Question>) {
        questionTitleTv.text = getContext().getString(R.string.out_of_text_placeholders, currentQuestion + 1, questions.size)
        val question = questions[currentQuestion]
        questionTv.text = question.question
        initAdapter(question.answers)
    }

    private fun initAdapter(answers: List<Answer>) {
        answersRecyclerAdapter = AnswersRecyclerAdapter(answers, this, viewMvcFactory)
        answersRecyclerView.adapter = answersRecyclerAdapter
    }

    override fun showButtonNext(show: Boolean) {
        buttonNext.visibility = if (show) View.VISIBLE else View.GONE
    }

    override fun onAnswerClicked(answer: Answer, answersViewMvc: IAnswersItemViewMvc) {
        for (listener in getListeners()){
            listener.onAnswerClicked(answer, answersViewMvc)
        }
    }

    override fun onCorrectAnswerFound(answersItemViewMvc: IAnswersItemViewMvc) {
        for (listener in getListeners()){
            listener.onCorrectAnswerFound(answersItemViewMvc)
        }
    }

    override fun updateCorrectQuestion() {
        answersRecyclerAdapter.updateCorrectQuestion()
    }

    companion object {
        private const val HUNDRED_PERCENT = 100
        private const val ALPHA_HUNDRED_PERCENT = 1.0f
        private const val ALPHA_FIFTY_PERCENT = 0.5f
    }
}