package com.dosparta.triviagame.categories

import com.android.volley.Request
import com.dosparta.triviagame.networking.ITriviaApiEndpoints
import com.dosparta.triviagame.networking.IVolleySingleton
import com.dosparta.triviagame.networking.parsers.CategoriesSchemaParser
import com.dosparta.triviagame.networking.parsers.ICategoriesSchemaParser
import com.dosparta.triviagame.networking.schemas.TriviaCategoriesScheme
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.eq
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.lang.Exception
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

private const val STRING_URL = "https://opentdb.com/api_category.php"
private const val CATEGORIES = "{\"trivia_categories\":[{\"id\":9,\"name\":\"General Knowledge\"},{\"id\":10,\"name\":\"Entertainment: Books\"}]}"

internal class FetchTriviaCategoriesUseCaseTest {

    private lateinit var classUnderTest: FetchTriviaCategoriesUseCase
    private lateinit var volleySingletonMock: IVolleySingleton
    private lateinit var triviaApiEndpoints: ITriviaApiEndpoints
    private lateinit var categoriesSchemaParser: ICategoriesSchemaParser
    private lateinit var listener1: FetchTriviaCategoriesUseCase.Listener
    private lateinit var listener2: FetchTriviaCategoriesUseCase.Listener
    private lateinit var triviaCategoriesScheme: TriviaCategoriesScheme

    @BeforeEach
    internal fun setup() {
        volleySingletonMock = Mockito.mock(IVolleySingleton::class.java)
        triviaApiEndpoints = Mockito.mock(ITriviaApiEndpoints::class.java)
        categoriesSchemaParser = Mockito.mock(ICategoriesSchemaParser::class.java)
        listener1 = Mockito.mock(FetchTriviaCategoriesUseCase.Listener::class.java)
        listener2 = Mockito.mock(FetchTriviaCategoriesUseCase.Listener::class.java)
        classUnderTest = FetchTriviaCategoriesUseCase(volleySingletonMock, triviaApiEndpoints, categoriesSchemaParser)

        classUnderTest.registerListener(listener1)
        classUnderTest.registerListener(listener2)

        whenever(triviaApiEndpoints.getCategoriesEndpoint()).thenReturn(STRING_URL)
        triviaCategoriesScheme = Json.decodeFromString<TriviaCategoriesScheme>(CATEGORIES)
        whenever(categoriesSchemaParser.categoriesSchemeToCategories(any())).thenAnswer { listOf<TriviaCategoryIn>() }
    }

    private fun volleyNotifySuccess() {
        whenever(
            volleySingletonMock.addStringRequestToQueue(
                any(),
                any(),
                eq(classUnderTest)
            )
        ).thenAnswer {
            it.getArgument<IVolleySingleton.Listener>(2).notifySuccess(CATEGORIES)
        }
    }

    private fun volleyNotifyFailure() {
        whenever(
            volleySingletonMock.addStringRequestToQueue(
                any(),
                any(),
                eq(classUnderTest)
            )
        ).thenAnswer {
            it.getArgument<IVolleySingleton.Listener>(2).notifyFailure(Exception())
        }
    }

    @Test
    internal fun fetchTriviaCategoriesAndNotify_endpointPassed() {
        val intCaptor: ArgumentCaptor<Int> = ArgumentCaptor.forClass(Int::class.java)
        val stringCaptor: ArgumentCaptor<String> = ArgumentCaptor.forClass(String::class.java)

        classUnderTest.fetchTriviaCategoriesAndNotify()
        //catch params from volley

        verify(volleySingletonMock).addStringRequestToQueue(capture(intCaptor), capture(stringCaptor), any())
        assertEquals(STRING_URL, stringCaptor.value)
        assertEquals(Request.Method.GET, intCaptor.value)
    }

    @Test
    internal fun fetchTriviaCategoriesAndNotify_notifySuccess() {
        val categoriesCaptor1: ArgumentCaptor<List<TriviaCategoryIn>> = ArgumentCaptor.forClass(listOf<TriviaCategoryIn>()::class.java)
        val categoriesCaptor2: ArgumentCaptor<List<TriviaCategoryIn>> = ArgumentCaptor.forClass(listOf<TriviaCategoryIn>()::class.java)
        val categoriesCaptor3: ArgumentCaptor<TriviaCategoriesScheme> = ArgumentCaptor.forClass(TriviaCategoriesScheme::class.java)

        volleyNotifySuccess()
        classUnderTest.fetchTriviaCategoriesAndNotify()

        verify(listener1).onTriviaCategoriesFetched(capture(categoriesCaptor1))
        verify(listener2).onTriviaCategoriesFetched(capture(categoriesCaptor2))
        verify(categoriesSchemaParser).categoriesSchemeToCategories(capture(categoriesCaptor3))
        assertEquals(triviaCategoriesScheme, categoriesCaptor3.value)
    }

    @Test
    internal fun fetchTriviaCategoriesAndNotify_notifyFailure() {

        volleyNotifyFailure()
        classUnderTest.fetchTriviaCategoriesAndNotify()

        verify(listener1).onTriviaCategoriesFetchFailed()
        verify(listener2).onTriviaCategoriesFetchFailed()
    }

    private fun <T> capture(argumentCaptor: ArgumentCaptor<T>): T = argumentCaptor.capture()
}