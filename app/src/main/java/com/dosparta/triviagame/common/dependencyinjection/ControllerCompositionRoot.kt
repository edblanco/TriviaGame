package com.dosparta.triviagame.common.dependencyinjection

import android.app.Activity
import android.view.LayoutInflater
import com.dosparta.triviagame.networking.VolleySingleton
import com.dosparta.triviagame.questions.FetchTriviaQuestionsUseCase
import com.dosparta.triviagame.screens.common.ActivityUtils
import com.dosparta.triviagame.screens.common.popups.OverlayMessagesHelper
import com.dosparta.triviagame.screens.common.screensnavigator.ScreensNavigator
import com.dosparta.triviagame.screens.common.ViewMvcFactory
import com.dosparta.triviagame.screens.common.dialogs.DialogManager
import com.dosparta.triviagame.screens.trivia.ITriviaGameController
import com.dosparta.triviagame.screens.trivia.TriviaGameController

class ControllerCompositionRoot(private val compositionRoot: CompositionRoot, private val activity: Activity) {

    private fun getVolleyInstance(): VolleySingleton {
        return compositionRoot.getVolleyInstance(activity)
    }

    private fun getLayoutInflater(): LayoutInflater {
        return LayoutInflater.from(activity)
    }

    fun getViewMvcFactory(): ViewMvcFactory {
        return ViewMvcFactory(getLayoutInflater())
    }

    private fun getFetchTriviaQuestionsUseCase(): FetchTriviaQuestionsUseCase {
        return FetchTriviaQuestionsUseCase(getVolleyInstance())
    }

    fun getTriviaGameController(): ITriviaGameController {
        // todo: move screen navigator and MessagesDisplayer to ActivityUtils
        return TriviaGameController(getFetchTriviaQuestionsUseCase(), getScreensNavigator(), getDialogManager(), getMessagesDisplayer(), getActivityUtils())
    }

    private fun getDialogManager(): DialogManager {
        return DialogManager(activity)
    }

    private fun getScreensNavigator(): ScreensNavigator {
        return ScreensNavigator(activity)
    }

    private fun getMessagesDisplayer(): OverlayMessagesHelper {
        return OverlayMessagesHelper(activity)
    }

    private fun getActivityUtils(): ActivityUtils {
        return ActivityUtils(activity)
    }
}