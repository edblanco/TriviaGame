package com.dosparta.triviagame.screens.trivia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView
import com.dosparta.triviagame.R
import com.dosparta.triviagame.questions.Answer

class AnswersRecyclerAdapter(private val answers: List<Answer>, private val listener: OnCorrectAnswerListener) :
    RecyclerView.Adapter<AnswersRecyclerAdapter.ViewHolder>(), IAnswersItemViewMvc.Listener {

    private var _answersItemViewMvc: IAnswersItemViewMvc? = null
    private val answersItemViewMvc get() = _answersItemViewMvc!!

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        _answersItemViewMvc = AnswersItemViewMvc(LayoutInflater.from(parent.context), parent)
        answersItemViewMvc.registerListener(this)

        return ViewHolder(answersItemViewMvc)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val answer = answers.elementAt(position)
        answersItemViewMvc.bindAnswer(answer)
    }

    override fun onAnswerClicked(answer: Answer) {
        val isCorrect = answer.correct
        answersItemViewMvc.updateTintColor(isCorrect)
        if (!isCorrect)
            updateCorrectQuestion()
        listener.onCorrect(isCorrect)
    }

    private fun updateCorrectQuestion() {
        for (i in answers.indices) {
            if (answers[i].correct) {
                notifyItemChanged(i, HIGHLIGHT_CORRECT_ANSWER)
                break
            }
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        for (payload in payloads){
            if (payload is String && payload == HIGHLIGHT_CORRECT_ANSWER) {
                answersItemViewMvc.updateTintColor(true)
                break
            }
        }
    }

    override fun getItemCount(): Int {
        return answers.size
    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        answersItemViewMvc.unregisterListener(this)
        super.onDetachedFromRecyclerView(recyclerView)
    }

    class ViewHolder (viewMvc: IAnswersItemViewMvc) : RecyclerView.ViewHolder(viewMvc.getRootView()) {
        private val viewMvc: IAnswersItemViewMvc

        init {
            this.viewMvc = viewMvc
        }
    }

    companion object {
        private const val HIGHLIGHT_CORRECT_ANSWER = "HIGHLIGHT_CORRECT_ANSWER"
    }
}