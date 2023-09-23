package com.dosparta.triviagame.networking.schemas

import kotlinx.serialization.Serializable

@Serializable
data class TriviaCategoriesScheme(
    val trivia_categories: List<TriviaCategory>,
)

@Serializable
data class TriviaCategory(
    val id: Long,
    val name: String,
)
