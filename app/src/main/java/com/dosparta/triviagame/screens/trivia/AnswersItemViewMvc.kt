package com.dosparta.triviagame.screens.trivia

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.dosparta.triviagame.R
import com.dosparta.triviagame.questions.Answer
import com.dosparta.triviagame.screens.trivia.IAnswersItemViewMvc.Listener

class AnswersItemViewMvc(layoutInflater: LayoutInflater, parent: ViewGroup) : IAnswersItemViewMvc {

    private var rootView: View
    private val listeners: MutableList<Listener> = mutableListOf()

    init {
        rootView = layoutInflater.inflate(R.layout.question_row_item, parent, false)
    }

    override fun registerListener(listener: Listener) {
        listeners.add(listener)
    }

    override fun unregisterListener(listener: Listener) {
        listeners.remove(listener)
    }

    override fun getRootView(): View {
        return rootView
    }

    override fun updateTintColor(holder: AnswersRecyclerAdapter.ViewHolder, isCorrect: Boolean) {
        val context = holder.questionButton.context
        val color = if (isCorrect) R.color.hunter_green else R.color.blood_red
        holder.questionButton.backgroundTintList = context.resources.getColorStateList(color, null)
    }

    override fun bindAnswer(answer: Answer, holder: AnswersRecyclerAdapter.ViewHolder) {
        holder.questionButton.text = answer.answer
        holder.questionButton.setOnClickListener {
            for (listener in listeners) {
                listener.onAnswerClicked(answer, holder)
            }
        }
    }
}