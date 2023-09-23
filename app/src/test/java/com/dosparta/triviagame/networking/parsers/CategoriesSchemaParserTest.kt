package com.dosparta.triviagame.networking.parsers

import com.dosparta.triviagame.networking.schemas.TriviaCategoriesScheme
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.json.Json

import org.junit.jupiter.api.Test

private const val CATEGORIES = "{\"trivia_categories\":[{\"id\":9,\"name\":\"General Knowledge\"},{\"id\":10,\"name\":\"Entertainment: Books\"}]}"

class CategoriesSchemaParserTest {

    private lateinit var classUnderTest: CategoriesSchemaParser
    private lateinit var triviaCategoriesScheme: TriviaCategoriesScheme

    @BeforeEach
    fun setup() {
        classUnderTest = CategoriesSchemaParser()
        triviaCategoriesScheme = Json.decodeFromString<TriviaCategoriesScheme>(CATEGORIES)
    }

    @Test
    fun categoriesSchemeToCategories() {
        //Arrange
        //Act
        val categoriesInList = classUnderTest.categoriesSchemeToCategories(triviaCategoriesScheme)
        //Assert
        assertTrue(triviaCategoriesScheme.trivia_categories.size == categoriesInList.size)
        for ((pos, category) in categoriesInList.withIndex()) {
            assertEquals(triviaCategoriesScheme.trivia_categories[pos].id.toInt(), category.id)
            assertEquals(triviaCategoriesScheme.trivia_categories[pos].name, category.name)
        }
    }
}