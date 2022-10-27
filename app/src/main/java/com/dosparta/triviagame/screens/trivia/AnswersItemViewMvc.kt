package com.dosparta.triviagame.screens.trivia

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.dosparta.triviagame.R
import com.dosparta.triviagame.questions.Answer
import com.dosparta.triviagame.screens.common.BaseViewMvc
import com.dosparta.triviagame.screens.trivia.IAnswersItemViewMvc.Listener

class AnswersItemViewMvc(layoutInflater: LayoutInflater, parent: ViewGroup) : BaseViewMvc(), IAnswersItemViewMvc {

    private val listeners: MutableList<Listener> = mutableListOf()
    private val questionButton: Button

    init {
        setRootView(layoutInflater.inflate(R.layout.question_row_item, parent, false))
        questionButton = findViewById(R.id.button_question)

    }

    override fun registerListener(listener: Listener) {
        listeners.add(listener)
    }

    override fun unregisterListener(listener: Listener) {
        listeners.remove(listener)
    }

    override fun updateTintColor(isCorrect: Boolean) {
        val color = if (isCorrect) R.color.hunter_green else R.color.blood_red
        questionButton.backgroundTintList = getContext().resources.getColorStateList(color, null)
    }

    override fun bindAnswer(answer: Answer) {
        questionButton.text = answer.answer
        questionButton.setOnClickListener {
            for (listener in listeners) {
                listener.onAnswerClicked(answer, this)
            }
        }
    }
}