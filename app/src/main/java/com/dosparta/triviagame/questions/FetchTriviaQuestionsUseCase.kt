package com.dosparta.triviagame.questions

import com.android.volley.Request
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

    //todo: test it against config changed
    fun fetchTriviaQuestionsAndNotify(questionsAmount: String) {
        val questionsEndpoint = triviaApiEndpoints.getQuestionsEndpoint(questionsAmount)
        volleyInstance.addStringRequestToQueue(Request.Method.GET, questionsEndpoint, this)
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