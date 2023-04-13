package com.dosparta.triviagame.networking

import com.dosparta.triviagame.networking.parsers.HtmlParser
import com.dosparta.triviagame.networking.schemas.QuestionsSchema
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.whenever

// todo change SUT for classUnderTest in all test
internal class QuestionsSchemaParserTest {

    companion object {
        private const val QUESTION_1 = "What was the character Kirby originally going to be named?"
        private const val CORRECT_ANSWER_1 = "Popopo"
        private const val INCORRECT_ANSWER_1_1 = "Dedede"
        private const val INCORRECT_ANSWER_1_2 = "Waddle Dee"
        private const val INCORRECT_ANSWER_1_3 = "Prince Puff"
        private const val DIFFICULTY_1 = "medium"
        private const val TYPE_1 = "multiple"
        private const val CATEGORY_1 = "Entertainment: Video Games"

        private const val CORRECT_ANSWER_2 = "Correct answer two"
        private const val QUESTION_2 = "Question two?"
        private const val INCORRECT_ANSWER_2_1 = "INCORRECT_ANSWER_2_1"
        private const val INCORRECT_ANSWER_2_2 = "INCORRECT_ANSWER_2_2"
        private const val INCORRECT_ANSWER_2_3 = "INCORRECT_ANSWER_2_3"
        private const val DIFFICULTY_2 = "DIFFICULTY_2"
        private const val TYPE_2 = "TYPE_2"
        private const val CATEGORY_2 = "CATEGORY_2"
    }

    private lateinit var classUnderTest: QuestionsSchemaParser
    private lateinit var questionsSchema: QuestionsSchema
    private lateinit var htmlParserMock: HtmlParser

    @BeforeEach
    fun setup() {
        htmlParserMock = Mockito.mock(HtmlParser::class.java)
        classUnderTest = QuestionsSchemaParser(htmlParserMock)
        questionsSchema = getQuestionsSchema()
        whenever(htmlParserMock.fromHtml(any(), any())).thenAnswer { i -> i.arguments[0] }
    }

    private fun getQuestionsSchema(): QuestionsSchema {
        val answers1 = listOf(INCORRECT_ANSWER_1_1, INCORRECT_ANSWER_1_2, INCORRECT_ANSWER_1_3)
        val answers2 = listOf(INCORRECT_ANSWER_2_1, INCORRECT_ANSWER_2_2, INCORRECT_ANSWER_2_3)
        val results = listOf(
            com.dosparta.triviagame.networking.schemas.Result(
                CATEGORY_1, CORRECT_ANSWER_1, DIFFICULTY_1, answers1, QUESTION_1, TYPE_1
            ), com.dosparta.triviagame.networking.schemas.Result(
                CATEGORY_2, CORRECT_ANSWER_2, DIFFICULTY_2, answers2, QUESTION_2, TYPE_2
            )
        )
        questionsSchema = QuestionsSchema(
            response_code = 0,
            results
        )
        return questionsSchema
    }

    @Test
    fun questionsSchemaToQuestions_success_containsOuterInfo() {
        val questionList = classUnderTest.questionsSchemaToQuestions(questionsSchema)

        assertFalse(questionList.isEmpty())
        assertNotNull(questionList.find { it.question == QUESTION_1 })
        assertNotNull(questionList.find { it.question == QUESTION_2 })
        assertNotNull(questionList.find { it.category == CATEGORY_1 })
        assertNotNull(questionList.find { it.category == CATEGORY_2 })
        assertNotNull(questionList.find { it.difficulty == DIFFICULTY_1 })
        assertNotNull(questionList.find { it.difficulty == DIFFICULTY_2 })
        assertNotNull(questionList.find { it.type == TYPE_1 })
        assertNotNull(questionList.find { it.type == TYPE_2 })
    }

    @Test
    fun questionsSchemaToQuestions_success_containsAnswers() {
        val questionList = classUnderTest.questionsSchemaToQuestions(questionsSchema)

        assertFalse(questionList.isEmpty())

        for (question in questionList) {
            val answers = question.answers
            if (question.question == QUESTION_1) {
                assertNotNull(answers.find { it.answer == CORRECT_ANSWER_1 })
                assertNotNull(answers.find { it.answer == INCORRECT_ANSWER_1_1 })
                assertNotNull(answers.find { it.answer == INCORRECT_ANSWER_1_2 })
                assertNotNull(answers.find { it.answer == INCORRECT_ANSWER_1_3 })
            } else if (question.question == QUESTION_2) {
                assertNotNull(answers.find { it.answer == CORRECT_ANSWER_2 })
                assertNotNull(answers.find { it.answer == INCORRECT_ANSWER_2_1 })
                assertNotNull(answers.find { it.answer == INCORRECT_ANSWER_2_2 })
                assertNotNull(answers.find { it.answer == INCORRECT_ANSWER_2_3 })
            }
        }
    }

    @Test
    fun questionsSchemaToQuestions_success_correctAnswers() {
        val questionList = classUnderTest.questionsSchemaToQuestions(questionsSchema)
        assertFalse(questionList.isEmpty())

        for (question in questionList) {
            for (answer in question.answers) {
                if (answer.answer == CORRECT_ANSWER_1 || answer.answer == CORRECT_ANSWER_2) assertTrue(
                    answer.correct
                ) else assertFalse(answer.correct)
            }
        }
    }
}