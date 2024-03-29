package com.dosparta.triviagame.screens.trivia.answersitem

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.dosparta.triviagame.questions.Answer
import com.dosparta.triviagame.screens.common.ViewMvcFactory

class AnswersRecyclerAdapter(
    private val answers: List<Answer>,
    private val listener: OnCorrectAnswerListener,
    private val viewMvcFactory: ViewMvcFactory
) :
    RecyclerView.Adapter<AnswersRecyclerAdapter.ViewHolder>(), IAnswersItemViewMvc.Listener {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val answersItemViewMvc: IAnswersItemViewMvc = viewMvcFactory.getAnswersItemViewMvc(parent)
        answersItemViewMvc.registerListener(this)

        return ViewHolder(answersItemViewMvc)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val answer = answers.elementAt(position)
        holder.answersItemViewMvc.bindAnswer(answer)
    }

    override fun onAnswerClicked(answer: Answer, viewMvc: IAnswersItemViewMvc) {
        listener.onAnswerClicked(answer, viewMvc)
    }

    fun updateCorrectQuestion() {
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
                holder.answersItemViewMvc.updateTintColor(true)
                listener.onCorrectAnswerFound(holder.answersItemViewMvc)
                break
            }
        }
    }

    override fun getItemCount(): Int {
        return answers.size
    }

    class ViewHolder (viewMvc: IAnswersItemViewMvc) : RecyclerView.ViewHolder(viewMvc.getRootView()) {
        internal val answersItemViewMvc: IAnswersItemViewMvc

        init {
            this.answersItemViewMvc = viewMvc
        }
    }

    companion object {
        private const val HIGHLIGHT_CORRECT_ANSWER = "HIGHLIGHT_CORRECT_ANSWER"
    }
}