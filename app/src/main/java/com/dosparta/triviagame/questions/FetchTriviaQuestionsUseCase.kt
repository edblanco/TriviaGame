package com.dosparta.triviagame.questions

import android.text.Html
import android.util.Log
import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.dosparta.triviagame.common.BaseObservable
import com.dosparta.triviagame.common.Utils
import com.dosparta.triviagame.networking.VolleySingleton
import com.dosparta.triviagame.networking.schemas.QuestionsSchema
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


class FetchTriviaQuestionsUseCase(private val volleyInstance: VolleySingleton) :
    BaseObservable<FetchTriviaQuestionsUseCase.Listener>() {

    interface Listener {
        fun onTriviaQuestionsFetched(questions: List<Question>)
        fun onTriviaQuestionsFetchFailed(error: Exception?) //todo re-work error. Activity should not know how questions are fetched
    }

    fun fetchTriviaQuestionsAndNotify(questionsAmount: String) {
        val stringRequest =
            StringRequest(Request.Method.GET, Utils.TRIVIA_API_URL + questionsAmount, { response ->
                notifySuccess(parseResponse(response))
            }, {
                Log.i(TAG, "fetchTriviaQuestionsAndNotify (network error): ${it.networkResponse}")
                notifyFailure(it)
            })
        volleyInstance.addToRequestQueue(stringRequest)
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