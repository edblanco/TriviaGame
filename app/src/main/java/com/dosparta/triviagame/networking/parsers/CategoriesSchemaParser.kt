package com.dosparta.triviagame.networking.parsers

import com.dosparta.triviagame.categories.TriviaCategoryIn
import com.dosparta.triviagame.networking.schemas.TriviaCategoriesScheme
import com.dosparta.triviagame.networking.schemas.TriviaCategory

class CategoriesSchemaParser : ICategoriesSchemaParser {
    override fun categoriesSchemeToCategories(triviaCategoriesScheme: TriviaCategoriesScheme): List<TriviaCategoryIn> {
        val categoriesList: MutableList<TriviaCategoryIn> = mutableListOf()
        for (category in triviaCategoriesScheme.trivia_categories) {
            val categoryIn = getCategory(category)
            categoriesList.add(categoryIn)
        }
        return categoriesList
    }

    private fun getCategory(category: TriviaCategory): TriviaCategoryIn {
        return TriviaCategoryIn(category.id.toInt(), category.name)
    }
}