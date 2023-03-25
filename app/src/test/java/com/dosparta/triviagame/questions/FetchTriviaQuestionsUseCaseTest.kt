package com.dosparta.triviagame.questions

import com.android.volley.Request
import com.android.volley.toolbox.StringRequest
import com.dosparta.triviagame.networking.IQuestionsSchemaParser
import com.dosparta.triviagame.networking.ITriviaApiEndpoints
import com.dosparta.triviagame.networking.IVolleySingleton
import com.dosparta.triviagame.networking.schemas.QuestionsSchema
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import java.net.URI

internal class FetchTriviaQuestionsUseCaseTest {

    companion object {
        const val TRIVIA_API_URL = "https://opentdb.com/api.php?amount="
        const val QUESTIONS_AMOUNT = "10"
        const val RESPONSE =
            "{\"response_code\":0,\"results\":[{\"category\":\"Entertainment: Video Games\",\"type\":\"multiple\",\"difficulty\":\"medium\",\"question\":\"What was the character Kirby originally going to be named?\",\"correct_answer\":\"Popopo\",\"incorrect_answers\":[\"Dedede\",\"Waddle Dee\",\"Prince Puff\"]},{\"category\":\"Art\",\"type\":\"boolean\",\"difficulty\":\"easy\",\"question\":\"Leonardo da Vinci&#039;s Mona Lisa does not have eyebrows.\",\"correct_answer\":\"True\",\"incorrect_answers\":[\"False\"]},{\"category\":\"Entertainment: Video Games\",\"type\":\"multiple\",\"difficulty\":\"medium\",\"question\":\"Which was the first video game to be produced by development company Rare?\",\"correct_answer\":\"Slalom\",\"incorrect_answers\":[\"R.C. Pro-Am\",\"Donkey Kong Country\",\"Battletoads\"]},{\"category\":\"Entertainment: Video Games\",\"type\":\"multiple\",\"difficulty\":\"easy\",\"question\":\"Which of the following was NOT a playable character in the game Kingdom Hearts: Birth by Sleep?\",\"correct_answer\":\"Ignis\",\"incorrect_answers\":[\"Ventus\",\"Terra\",\"Aqua\"]},{\"category\":\"Animals\",\"type\":\"multiple\",\"difficulty\":\"medium\",\"question\":\"What is the common term for bovine spongiform encephalopathy (BSE)?\",\"correct_answer\":\"Mad Cow disease\",\"incorrect_answers\":[\"Weil&#039;s disease\",\"Milk fever\",\"Foot-and-mouth disease\"]}]}"
    }

    private lateinit var SUT: FetchTriviaQuestionsUseCase;
    private lateinit var volleySingletonTd: VolleySingletonTd
    private lateinit var triviaApiEndpointTd: TriviaApiEndpointsTd
    private lateinit var listener: FetchTriviaListener
    private lateinit var listener2: FetchTriviaListener
    private lateinit var questionsSchemaParser: IQuestionsSchemaParser

    @BeforeEach
    internal fun setUp() {
        volleySingletonTd = VolleySingletonTd()
        triviaApiEndpointTd = TriviaApiEndpointsTd()
        questionsSchemaParser = QuestionsSchemaParserTd()
        SUT = FetchTriviaQuestionsUseCase(
            volleySingletonTd,
            triviaApiEndpointTd,
            questionsSchemaParser
        )
        listener = FetchTriviaListener()
        listener2 = FetchTriviaListener()
    }

    // same amount is passed to volley request
    @Test
    internal fun fetchTriviaQuestionsAndNotify_questionsAmountPassed() {
        SUT.fetchTriviaQuestionsAndNotify(QUESTIONS_AMOUNT)

        assertEquals(volleySingletonTd.questionsAmount, QUESTIONS_AMOUNT)
    }

    // endpoint is pass to request volley
    @Test
    internal fun fetchTriviaQuestionsAndNotify_endpointPassed() {
        SUT.fetchTriviaQuestionsAndNotify(QUESTIONS_AMOUNT)

        assertEquals(
            volleySingletonTd.url,
            triviaApiEndpointTd.getQuestionsEndpoint(QUESTIONS_AMOUNT)
        )
    }

    // Notify error when wrong response is provided
    @Test
    internal fun onTriviaQuestionsFetchFailed_wrongResponse_notifyError() {
        SUT.registerListener(listener)
        volleySingletonTd.callNotifySuccess = true
        volleySingletonTd.response = "wrong response"
        volleySingletonTd.createStringRequest(Request.Method.GET, TRIVIA_API_URL, SUT)

        assertTrue(listener.onTriviaQuestionsFetchFailed)
        assertFalse(listener.onTriviaQuestionsFetched)
    }

    //notify success when successful
    @Test
    internal fun onTriviaQuestionsFetched_properResponse_notifySuccess() {
        SUT.registerListener(listener)
        volleySingletonTd.callNotifySuccess = true
        volleySingletonTd.response = RESPONSE
        volleySingletonTd.createStringRequest(Request.Method.GET, TRIVIA_API_URL, SUT)

        assertTrue(listener.onTriviaQuestionsFetched)
        assertFalse(listener.onTriviaQuestionsFetchFailed)
    }

    // notify error when failed
    @Test
    internal fun onTriviaQuestionsFetchFailed_failure_notifyError() {
        SUT.registerListener(listener)
        volleySingletonTd.callNotifyError = true
        volleySingletonTd.createStringRequest(Request.Method.GET, TRIVIA_API_URL, SUT)

        assertTrue(listener.onTriviaQuestionsFetchFailed)
        assertFalse(listener.onTriviaQuestionsFetched)
    }

    // make sure that for notify methods (notify success and error) all listeners all called
    @Test
    internal fun multipleListeners_notifyAllListeners() {
        SUT.registerListener(listener)
        SUT.registerListener(listener2)
        volleySingletonTd.callNotifySuccess = true
        volleySingletonTd.callNotifyError = true
        volleySingletonTd.response = RESPONSE
        volleySingletonTd.createStringRequest(Request.Method.GET, TRIVIA_API_URL, SUT)

        assertTrue(listener.onTriviaQuestionsFetched)
        assertTrue(listener2.onTriviaQuestionsFetched)
        assertTrue(listener.onTriviaQuestionsFetchFailed)
        assertTrue(listener2.onTriviaQuestionsFetchFailed)
    }


    private class VolleySingletonTd : IVolleySingleton {
        var questionsAmount: String = ""
        var url = ""
        var callNotifySuccess = false
        var callNotifyError = false
        var response = ""

        override fun <T> addToRequestQueue(req: Request<T>) {
            url = req.url
            //val url = Uri.parse(req.url)
            //questionsAmount = url.getQueryParameter("amount") ?: ""
        }

        override fun createStringRequest(
            requestMethod: Int,
            url: String,
            listener: IVolleySingleton.Listener
        ): StringRequest? {
            val uri = URI(url)
            this.url = url
            questionsAmount = uri.findParameterValue("amount") ?: ""
            if (callNotifySuccess) {
                listener.notifySuccess(response)
            }
            if (callNotifyError) {
                listener.notifyFailure(java.lang.Exception())
            }
            return null
        }

    }

    private class TriviaApiEndpointsTd : ITriviaApiEndpoints {
        override fun getQuestionsEndpoint(amount: String): String {
            return TRIVIA_API_URL + amount
        }

    }

    private class FetchTriviaListener : FetchTriviaQuestionsUseCase.Listener {
        var onTriviaQuestionsFetched = false
        var onTriviaQuestionsFetchFailed = false

        override fun onTriviaQuestionsFetched(questions: List<Question>) {
            onTriviaQuestionsFetched = true
        }

        override fun onTriviaQuestionsFetchFailed(error: Exception?) {
            onTriviaQuestionsFetchFailed = true
        }

    }

    private class QuestionsSchemaParserTd : IQuestionsSchemaParser {
        override fun questionsSchemaToQuestions(questionsSchema: QuestionsSchema): List<Question> {
            return listOf()
        }

    }
}

private fun URI.findParameterValue(parameterName: String): String? {
    return rawQuery.split('&').map {
        val parts = it.split('=')
        val name = parts.firstOrNull() ?: ""
        val value = parts.drop(1).firstOrNull() ?: ""
        Pair(name, value)
    }.firstOrNull { it.first == parameterName }?.second
}