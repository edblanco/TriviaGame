package com.dosparta.triviagame.questions

import android.text.Html
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.dosparta.triviagame.common.BaseObservable
import com.dosparta.triviagame.networking.ITriviaApiEndpoints
import com.dosparta.triviagame.networking.IVolleySingleton
import com.dosparta.triviagame.networking.schemas.QuestionsSchema
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


class FetchTriviaQuestionsUseCase(
    private val volleyInstance: IVolleySingleton,
    private val triviaApiEndpoints: ITriviaApiEndpoints
) :
    BaseObservable<FetchTriviaQuestionsUseCase.Listener>() {

    interface Listener {
        fun onTriviaQuestionsFetched(questions: List<Question>)
        fun onTriviaQuestionsFetchFailed(error: Exception?)
    }


    fun fetchTriviaQuestionsAndNotify(questionsAmount: String) {
        val questionsEndpoint = triviaApiEndpoints.getQuestionsEndpoint(questionsAmount).toString()
        volleyInstance.addToRequestQueue(getStringRequest(Request.Method.GET, questionsEndpoint))
    }

    private fun getStringRequest(requestMethod: Int, url: String): StringRequest {
        return StringRequest(requestMethod, url, { response ->
            notifySuccess(parseResponse(response))
        }, {
            Log.i(TAG, "fetchTriviaQuestionsAndNotify (network error): ${it.networkResponse}")
            notifyFailure(it)
        })
    }

    private fun notifySuccess(questions: List<Question>) {
        for (listener in listeners) {
            listener.onTriviaQuestionsFetched(questions)
        }
    }

    private fun notifyFailure(error: Exception?) {
        for (listener in listeners) {
            listener.onTriviaQuestionsFetchFailed(error)
        }
    }

    private fun parseResponse(response: String): List<Question> {
        val jsonData = Json.decodeFromString<QuestionsSchema>(response)
        Log.i(TAG, "onStart: $jsonData")
        return questionsSchemaToQuestions(jsonData)
    }

    private fun questionsSchemaToQuestions(questionsSchema: QuestionsSchema): List<Question> {
        val questionList: MutableList<Question> = mutableListOf()
        for (result in questionsSchema.results) {
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

            val question = Question(
                question = Html.fromHtml(result.question, Html.FROM_HTML_MODE_COMPACT).toString(),
                difficulty = result.difficulty,
                category = result.category,
                type = result.type,
                answers = answers.shuffled()
            )

            questionList.add(question)
        }
        Log.i(TAG, "list of question $questionList")
        return questionList
    }

    companion object {
        private val TAG = FetchTriviaQuestionsUseCase::class.java.simpleName
    }
}