package com.dosparta.triviagame.questions

import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.dosparta.triviagame.common.BaseObservable
import com.dosparta.triviagame.networking.IQuestionsSchemaParser
import com.dosparta.triviagame.networking.ITriviaApiEndpoints
import com.dosparta.triviagame.networking.IVolleySingleton
import com.dosparta.triviagame.networking.schemas.QuestionsSchema
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json


class FetchTriviaQuestionsUseCase(
    private val volleyInstance: IVolleySingleton,
    private val triviaApiEndpoints: ITriviaApiEndpoints,
    private val questionsSchemaParser: IQuestionsSchemaParser
) :
    BaseObservable<FetchTriviaQuestionsUseCase.Listener>(), IVolleySingleton.Listener {

    interface Listener {
        fun onTriviaQuestionsFetched(questions: List<Question>)
        fun onTriviaQuestionsFetchFailed(error: Exception?)
    }

    //todo: all volley dependencies should be removed from this class
    fun fetchTriviaQuestionsAndNotify(questionsAmount: String) {
        val questionsEndpoint = triviaApiEndpoints.getQuestionsEndpoint(questionsAmount)
        val volleyRequest = getStringRequest(Request.Method.GET, questionsEndpoint)
        volleyRequest?.let {
            volleyInstance.addToRequestQueue(it)
        }
    }

    // todo: move this stringRequest to volley class
    private fun getStringRequest(requestMethod: Int, url: String): StringRequest? {
        return volleyInstance.createStringRequest(requestMethod, url, this)
    }


    override fun notifySuccess(response: String) {
        try {
            notifySuccess(parseResponse(response))
        } catch (exc: Exception) {
            notifyFailure(exc)
        }
    }
    private fun notifySuccess(questions: List<Question>) {
        for (listener in listeners) {
            listener.onTriviaQuestionsFetched(questions)
        }
    }

    override fun notifyFailure(error: Exception?) {
        for (listener in listeners) {
            listener.onTriviaQuestionsFetchFailed(error)
        }
    }

    private fun parseResponse(response: String): List<Question> {
        val jsonData = Json.decodeFromString<QuestionsSchema>(response)
        return questionsSchemaParser.questionsSchemaToQuestions(jsonData)
    }
}