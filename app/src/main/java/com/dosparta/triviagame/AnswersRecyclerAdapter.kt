package com.dosparta.triviagame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

private const val HIGHLIGHT_CORRECT_ANSWER = "HIGHLIGHT_CORRECT_ANSWER"

class AnswersRecyclerAdapter(private val answers: List<Answer>, private val listener: OnCorrectAnswerListener) :
    RecyclerView.Adapter<AnswersRecyclerAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.question_row_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.questionButton.text = answers.elementAt(position).answer
        holder.questionButton.setOnClickListener {
            handleButtonClicked(position, holder)
        }
    }

    private fun handleButtonClicked(position: Int, holder: ViewHolder) {
        val isCorrect = answers.elementAt(position).correct
        updateTintColor(holder, isCorrect)
        if (!isCorrect)
            updateCorrectQuestion()
        listener.onCorrect(isCorrect)
    }

    private fun updateTintColor(holder: ViewHolder, isCorrect: Boolean) {
        val context = holder.questionButton.context
        val color = if (isCorrect) R.color.hunter_green else R.color.blood_red
        holder.questionButton.backgroundTintList = context.resources.getColorStateList(color, null)
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
                updateTintColor(holder, true)
                break
            }
        }
    }

    override fun getItemCount(): Int {
        return answers.size
    }

    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val questionButton: Button

        init {
            questionButton = view.findViewById(R.id.button_question)
        }
    }
}

interface OnCorrectAnswerListener{
    fun onCorrect(isCorrect: Boolean)
}