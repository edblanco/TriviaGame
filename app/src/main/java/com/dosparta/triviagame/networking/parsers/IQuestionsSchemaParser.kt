package com.dosparta.triviagame.networking.parsers

import com.dosparta.triviagame.networking.schemas.QuestionsSchema
import com.dosparta.triviagame.questions.Question

interface IQuestionsSchemaParser {
    fun questionsSchemaToQuestions(questionsSchema: QuestionsSchema): List<Question>
}