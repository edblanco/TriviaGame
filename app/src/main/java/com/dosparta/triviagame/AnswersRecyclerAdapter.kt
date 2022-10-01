package com.dosparta.triviagame

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.RecyclerView

class AnswersRecyclerAdapter(private val answers: List<Answer>, private val listener: OnCorrectAnswerListener) :
    RecyclerView.Adapter<AnswersRecyclerAdapter.ViewHolder>() {
    class ViewHolder (view: View) : RecyclerView.ViewHolder(view) {
        val questionButton: Button

        init {
            questionButton = view.findViewById(R.id.button_question)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.question_row_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.questionButton.text = answers.elementAt(position).answer
        holder.questionButton.setOnClickListener {
            val isCorrect = answers.elementAt(position).correct
            updateTintColor(holder, isCorrect)
            if (!isCorrect){
                for (i in answers.indices) {
                    if (answers[i].correct) {
                        notifyItemChanged(i, "markCorrect")
                        break
                    }
                }
            }
            listener.onCorrect(isCorrect)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        super.onBindViewHolder(holder, position, payloads)
        for (payload in payloads){
            if (payload is String && (payload as String).equals("markCorrect")) {
                updateTintColor(holder, true)
                break
            }
        }
    }

    private fun updateTintColor(holder: ViewHolder, isCorrect: Boolean) {
        val context = holder.questionButton.context
        val color = if (isCorrect) R.color.hunter_green else R.color.blood_red
        holder.questionButton.backgroundTintList = context.resources.getColorStateList(color, null)
    }

    override fun getItemCount(): Int {
        return answers.size
    }
}

public interface OnCorrectAnswerListener{
    fun onCorrect(isCorrect: Boolean)
}