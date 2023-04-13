package com.dosparta.triviagame.networking

import android.text.Html
import com.dosparta.triviagame.networking.parsers.IHtmlParser
import com.dosparta.triviagame.networking.schemas.QuestionsSchema
import com.dosparta.triviagame.networking.schemas.Result
import com.dosparta.triviagame.questions.Answer
import com.dosparta.triviagame.questions.Question

class QuestionsSchemaParser(private val htmlParser: IHtmlParser) : IQuestionsSchemaParser {

    override fun questionsSchemaToQuestions(questionsSchema: QuestionsSchema): List<Question> {
        val questionList: MutableList<Question> = mutableListOf()
        for (result in questionsSchema.results) {
            val question = getQuestion(result, getAnswers(result))
            questionList.add(question)
        }
        return questionList
    }

    private fun getAnswers(result: Result): List<Answer> {
        val correctAnswer = htmlParser.fromHtml(result.correct_answer, Html.FROM_HTML_MODE_COMPACT)
        val answers: MutableList<Answer> = mutableListOf(
            Answer(correctAnswer, true)
        )
        for (incorrectAnswer in result.incorrect_answers) {
            val schemaAnswer = htmlParser.fromHtml(incorrectAnswer, Html.FROM_HTML_MODE_COMPACT)
            answers.add(
                Answer(schemaAnswer, false)
            )
        }
        return answers
    }

    private fun getQuestion(result: Result, answers: List<Answer>): Question {
        val question = htmlParser.fromHtml(result.question, Html.FROM_HTML_MODE_COMPACT)
        return Question(
            question = question,
            difficulty = result.difficulty,
            category = result.category,
            type = result.type,
            answers = answers.shuffled()
        )
    }
}