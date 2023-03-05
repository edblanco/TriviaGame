package com.dosparta.triviagame.screens.trivia

import android.os.Bundle
import android.util.Log
import com.android.volley.*
import com.dosparta.triviagame.R
import com.dosparta.triviagame.common.dependencyinjection.ControllerCompositionRoot
import com.dosparta.triviagame.questions.Answer
import com.dosparta.triviagame.questions.FetchTriviaQuestionsUseCase
import com.dosparta.triviagame.questions.Question
import com.dosparta.triviagame.screens.common.dialogs.DialogsEventBus
import com.dosparta.triviagame.screens.common.dialogs.promptdialog.PromptDialogEvent
import com.dosparta.triviagame.screens.common.dialogs.questionsdialog.QuestionsDialogEvent
import com.dosparta.triviagame.screens.trivia.answersitem.IAnswersItemViewMvc

class TriviaGameController(controllerCompositionRoot: ControllerCompositionRoot) :
    ITriviaGameController, ITriviaGameViewMvc.Listener, FetchTriviaQuestionsUseCase.Listener,
    DialogsEventBus.Listener {

    companion object {
        private val tag = TriviaGameActivity::class.java.simpleName
        private const val JUMP_TO_NEXT_QUESTION_DELAY = 3000L
        private const val CURRENT_QUESTION = "CURRENT_QUESTION"
        private const val CORRECT_ANSWERS = "CORRECT_ANSWERS"
        private const val QUESTIONS = "QUESTIONS"
        private const val SCREEN_STATE = "SCREEN_STATE"

        private const val INITIAL_SETUP_DIALOG_TAG = "INITIAL_SETUP_DIALOG"
        private const val SHOW_RESULTS_DIALOG_TAG = "SHOW_RESULTS_DIALOG_TAG"
        private const val SHOW_ERROR_DIALOG_TAG = "SHOW_ERROR_DIALOG_TAG"
    }

    private enum class ScreenState {
        IDLE, INITIAL_SETUP_SHOWN, ANSWER_SELECTED
    }

    private var questions: List<Question> = listOf()
    private var currentQuestion: Int = 0
    private var correctAnswers: Int = 0

    private var _viewMvc: ITriviaGameViewMvc? = null
    private val viewMvc get() = _viewMvc!!

    private var screenState = ScreenState.IDLE

    private val fetchTriviaQuestionsUseCase =
        controllerCompositionRoot.getFetchTriviaQuestionsUseCase()
    private val screensNavigator = controllerCompositionRoot.getScreensNavigator()
    private val dialogsManager = controllerCompositionRoot.getDialogManager()
    private val activityUtils = controllerCompositionRoot.getActivityUtils()
    private val dialogsEventBus = controllerCompositionRoot.getDialogsEventBus()

    override fun onStart() {
        viewMvc.registerListener(this)
        fetchTriviaQuestionsUseCase.registerListener(this)
        dialogsEventBus.registerListener(this)
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        currentQuestion = savedInstanceState.getInt(CURRENT_QUESTION)
        correctAnswers = savedInstanceState.getInt(CORRECT_ANSWERS)
        questions =
            savedInstanceState.getParcelableArrayList<Question>(QUESTIONS)?.toList() ?: questions
        screenState = savedInstanceState.getSerializable(SCREEN_STATE) as ScreenState
    }

    override fun onResume() {
        if (questions.isEmpty()) {
            fetchQuestionsSetup()
        } else {
            onTriviaQuestionsFetched(questions)
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(CURRENT_QUESTION, currentQuestion)
        outState.putInt(CORRECT_ANSWERS, correctAnswers)
        outState.putParcelableArrayList(QUESTIONS, ArrayList(questions))
        outState.putSerializable(SCREEN_STATE, screenState)
    }

    override fun onStop() {
        viewMvc.unregisterListener(this)
        fetchTriviaQuestionsUseCase.unregisterListener(this)
        dialogsEventBus.unregisterListener(this)
    }

    private fun fetchQuestionsSetup() {
        if (screenState == ScreenState.IDLE) {
            screenState = ScreenState.INITIAL_SETUP_SHOWN
            dialogsManager.showQuestionsAmountUseCaseDialog(INITIAL_SETUP_DIALOG_TAG, false)
        }
    }

    override fun onTriviaQuestionsFetched(questions: List<Question>) {
        this.questions = questions
        viewMvc.bindQuestions(currentQuestion, questions)
        viewMvc.setLoadingState(false)
    }

    // todo On retry it should not ask the amount of questions again
    override fun onTriviaQuestionsFetchFailed(error: Exception?) {
        val message = getMessageFromError(error)
        showErrorDialog(message)
    }

    private fun getMessageFromError(error: Exception?): Int {
        return when (error) {
            is NoConnectionError, is AuthFailureError, is NetworkError -> {
                R.string.no_internet_connection_error_msg
            }
            is ServerError -> {
                R.string.server_error_msg
            }
            is ParseError -> {
                R.string.network_error_popup_msg
            }
            is TimeoutError -> {
                R.string.timeout_network_error_msg
            }
            else -> {
                R.string.network_error_popup_msg
            }
        }
    }

    override fun bindView(viewMvc: ITriviaGameViewMvc) {
        _viewMvc = viewMvc
    }

    private fun showErrorDialog(message: Int) {
        dialogsManager.showErrorUseCaseDialog(message, SHOW_ERROR_DIALOG_TAG, false)
    }

    override fun onAnswerClicked(answer: Answer, answersViewMvc: IAnswersItemViewMvc) {
        if (screenState == ScreenState.ANSWER_SELECTED) {
            Log.i(tag, "onAnswerClicked: screen state is $screenState")
            return
        }
        screenState = ScreenState.ANSWER_SELECTED
        handleSelectedAnswer(answer, answersViewMvc)
    }

    private fun handleSelectedAnswer(
        answer: Answer,
        answersViewMvc: IAnswersItemViewMvc
    ) {
        val isCorrect = answer.correct
        answersViewMvc.updateTintColor(answer.correct)

        if (isCorrect) {
            ++correctAnswers
        } else {
            viewMvc.updateCorrectQuestion()
        }

        viewMvc.showButtonNext(true)
        moveToNextQuestionWithDelay()
    }

    private fun moveToNextQuestionWithDelay() {
        activityUtils.postDelayed({
            moveToNextQuestion()
        }, JUMP_TO_NEXT_QUESTION_DELAY)
    }

    override fun onButtonNextClicked() {
        activityUtils.removeCallbacksAndMessages(null)
        moveToNextQuestion()
    }

    private fun moveToNextQuestion() {
        screenState = ScreenState.IDLE
        if (currentQuestion == (questions.size - 1)) {
            showResults()
            return
        }
        ++currentQuestion
        viewMvc.showButtonNext(false)
        viewMvc.bindQuestions(currentQuestion, questions)
    }

    private fun showResults() {
        dialogsManager.showResultsUseCaseDialog(
            correctAnswers, questions.size,
            SHOW_RESULTS_DIALOG_TAG, false
        )
    }

    private fun resetGame() {
        correctAnswers = 0
        currentQuestion = 0
        viewMvc.showButtonNext(false)
        viewMvc.bindQuestions(currentQuestion, questions)
    }

    override fun onCorrectAnswerFound(answersItemViewMvc: IAnswersItemViewMvc) {
        answersItemViewMvc.updateTintColor(true)
    }

    override fun onDialogEvent(event: Any) {
        val dialogTag = dialogsManager.getShownDialogTag()
        if (INITIAL_SETUP_DIALOG_TAG == dialogTag) {
            handleInitialSetupDialogEvent(event)
        } else if (SHOW_RESULTS_DIALOG_TAG == dialogTag) {
            handleResultsDialogEvent(event)
        } else if (SHOW_ERROR_DIALOG_TAG == dialogTag) {
            handleErrorDialogEvent(event)
        }
    }

    private fun handleInitialSetupDialogEvent(event: Any) {
        screenState = ScreenState.IDLE
        val questionsAmount = (event as QuestionsDialogEvent).getQuestionsAmount()
        fetchTriviaQuestionsUseCase.fetchTriviaQuestionsAndNotify(questionsAmount)
    }

    private fun handleResultsDialogEvent(event: Any) {
        (event as PromptDialogEvent).let {
            when (it.getClickedButton()) {
                PromptDialogEvent.Button.POSITIVE -> {
                    screensNavigator.toTriviaGame()
                    screensNavigator.closeScreen()
                }
                PromptDialogEvent.Button.NEGATIVE -> {
                    resetGame()
                }
            }
        }
    }

    private fun handleErrorDialogEvent(event: Any) {
        (event as PromptDialogEvent).let {
            when (it.getClickedButton()) {
                PromptDialogEvent.Button.POSITIVE -> {
                    screensNavigator.toTriviaGame()
                    screensNavigator.closeScreen()
                }
                PromptDialogEvent.Button.NEGATIVE -> {
                    screensNavigator.closeApp()
                }
            }
        }
    }
}