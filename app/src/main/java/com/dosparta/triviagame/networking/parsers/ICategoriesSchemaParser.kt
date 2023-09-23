package com.dosparta.triviagame.networking.parsers

import com.dosparta.triviagame.categories.TriviaCategoryIn
import com.dosparta.triviagame.networking.schemas.TriviaCategoriesScheme

interface ICategoriesSchemaParser {
    fun categoriesSchemeToCategories(triviaCategoriesScheme: TriviaCategoriesScheme): List<TriviaCategoryIn>
}