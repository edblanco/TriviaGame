package com.dosparta.triviagame.screens.trivia

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import com.dosparta.triviagame.R
import com.dosparta.triviagame.questions.Answer
import com.dosparta.triviagame.screens.common.BaseObservableViewMvc

class AnswersItemViewMvc(layoutInflater: LayoutInflater, parent: ViewGroup?) : BaseObservableViewMvc<IAnswersItemViewMvc.Listener>(), IAnswersItemViewMvc {

    private val questionButton: Button

    init {
        setRootView(layoutInflater.inflate(R.layout.question_row_item, parent, false))
        questionButton = findViewById(R.id.button_question)
    }

    override fun updateTintColor(isCorrect: Boolean) {
        val color = if (isCorrect) R.color.hunter_green else R.color.blood_red
        questionButton.backgroundTintList = getContext().resources.getColorStateList(color, null)
    }

    override fun bindAnswer(answer: Answer) {
        questionButton.text = answer.answer
        questionButton.setOnClickListener {
            for (listener in getListeners()) {
                listener.onAnswerClicked(answer, this)
            }
        }
    }
}