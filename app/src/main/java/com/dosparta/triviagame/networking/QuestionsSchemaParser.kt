package com.dosparta.triviagame.networking

import android.text.Html
import com.dosparta.triviagame.networking.schemas.QuestionsSchema
import com.dosparta.triviagame.networking.schemas.Result
import com.dosparta.triviagame.questions.Answer
import com.dosparta.triviagame.questions.Question

class QuestionsSchemaParser : IQuestionsSchemaParser {

    override fun questionsSchemaToQuestions(questionsSchema: QuestionsSchema): List<Question> {
        val questionList: MutableList<Question> = mutableListOf()
        for (result in questionsSchema.results) {
            val question = getQuestion(result, getAnswers(result))
            questionList.add(question)
        }
        return questionList
    }

    private fun getAnswers(result: Result): List<Answer> {
        val answers: MutableList<Answer> = mutableListOf(
            Answer(
                Html.fromHtml(
                    result.correct_answer,
                    Html.FROM_HTML_MODE_COMPACT
                ).toString(), true
            )
        )
        for (incorrectAnswer in result.incorrect_answers) {
            answers.add(
                Answer(
                    Html.fromHtml(incorrectAnswer, Html.FROM_HTML_MODE_COMPACT).toString(),
                    false
                )
            )
        }
        return answers
    }

    private fun getQuestion(result: Result, answers: List<Answer>): Question {
        return Question(
            question = Html.fromHtml(result.question, Html.FROM_HTML_MODE_COMPACT).toString(),
            difficulty = result.difficulty,
            category = result.category,
            type = result.type,
            answers = answers.shuffled()
        )
    }
}