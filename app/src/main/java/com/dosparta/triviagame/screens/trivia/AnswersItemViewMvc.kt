package com.dosparta.triviagame.screens.trivia

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.dosparta.triviagame.R
import com.dosparta.triviagame.questions.Answer
import com.dosparta.triviagame.screens.trivia.IAnswersItemViewMvc.Listener

class AnswersItemViewMvc(layoutInflater: LayoutInflater, parent: ViewGroup) : IAnswersItemViewMvc {

    private var rootView: View
    private val listeners: MutableList<Listener> = mutableListOf()
    private val questionButton: Button

    init {
        rootView = layoutInflater.inflate(R.layout.question_row_item, parent, false)
        questionButton = findViewById(R.id.button_question)

    }

    private fun <T: View> findViewById(id: Int) : T{
        return getRootView().findViewById(id)
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

    private fun getContext(): Context {
        return getRootView().context
    }

    override fun updateTintColor(isCorrect: Boolean) {
        val color = if (isCorrect) R.color.hunter_green else R.color.blood_red
        questionButton.backgroundTintList = getContext().resources.getColorStateList(color, null)
    }

    override fun bindAnswer(answer: Answer) {
        questionButton.text = answer.answer
        questionButton.setOnClickListener {
            for (listener in listeners) {
                listener.onAnswerClicked(answer)
            }
        }
    }
}