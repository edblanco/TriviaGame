package com.dosparta.triviagame.categories

import com.android.volley.Request
import com.dosparta.triviagame.common.BaseObservable
import com.dosparta.triviagame.networking.ITriviaApiEndpoints
import com.dosparta.triviagame.networking.IVolleySingleton
import com.dosparta.triviagame.networking.parsers.ICategoriesSchemaParser
import com.dosparta.triviagame.networking.schemas.TriviaCategoriesScheme
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

class FetchTriviaCategoriesUseCase(
    private val volleyInstance: IVolleySingleton,
    private val triviaApiEndpoints: ITriviaApiEndpoints,
    private val categoriesSchemaParser: ICategoriesSchemaParser
) : BaseObservable<FetchTriviaCategoriesUseCase.Listener>(), IVolleySingleton.Listener {

    interface Listener {
        fun onTriviaCategoriesFetched(categories: List<TriviaCategoryIn>)
        fun onTriviaCategoriesFetchFailed()
    }

    fun fetchTriviaCategoriesAndNotify() {
        val categoriesEndpoint = triviaApiEndpoints.getCategoriesEndpoint();
        volleyInstance.addStringRequestToQueue(Request.Method.GET, categoriesEndpoint, this)
    }

    override fun notifySuccess(response: String) {
        val triviaCategories = getCategories(response)
        for (listener in listeners) {
            listener.onTriviaCategoriesFetched(triviaCategories)
        }
    }

    private fun getCategories(response: String): List<TriviaCategoryIn> {
        val jsonData = Json.decodeFromString<TriviaCategoriesScheme>(response)
        return categoriesSchemaParser.categoriesSchemeToCategories(jsonData)
    }

    override fun notifyFailure(error: Exception?) {
        for (listener in listeners) {
            listener.onTriviaCategoriesFetchFailed()
        }
    }
}