package com.dosparta.triviagame.common.dependencyinjection

import android.view.LayoutInflater
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentManager
import com.dosparta.triviagame.categories.FetchTriviaCategoriesUseCase
import com.dosparta.triviagame.networking.*
import com.dosparta.triviagame.networking.parsers.CategoriesSchemaParser
import com.dosparta.triviagame.networking.parsers.HtmlParser
import com.dosparta.triviagame.networking.parsers.ICategoriesSchemaParser
import com.dosparta.triviagame.networking.parsers.IHtmlParser
import com.dosparta.triviagame.networking.parsers.IQuestionsSchemaParser
import com.dosparta.triviagame.networking.parsers.QuestionsSchemaParser
import com.dosparta.triviagame.questions.FetchTriviaQuestionsUseCase
import com.dosparta.triviagame.screens.common.ActivityUtils
import com.dosparta.triviagame.screens.common.ViewMvcFactory
import com.dosparta.triviagame.screens.common.dialogs.DialogsEventBus
import com.dosparta.triviagame.screens.common.dialogs.DialogsManager
import com.dosparta.triviagame.screens.common.popups.OverlayMessagesHelper
import com.dosparta.triviagame.screens.common.screensnavigator.ScreensNavigator
import com.dosparta.triviagame.screens.trivia.ITriviaGameController
import com.dosparta.triviagame.screens.trivia.TriviaGameController

class ControllerCompositionRoot(
    private val compositionRoot: CompositionRoot,
    private val activity: FragmentActivity
) {

    private fun getVolleyInstance(): IVolleySingleton {
        return compositionRoot.getVolleyInstance(activity)
    }

    private fun getLayoutInflater(): LayoutInflater {
        return LayoutInflater.from(activity)
    }

    fun getViewMvcFactory(): ViewMvcFactory {
        return ViewMvcFactory(getLayoutInflater())
    }

    private fun getFragmentManager(): FragmentManager {
        return activity.supportFragmentManager
    }

    fun getFetchTriviaQuestionsUseCase(): FetchTriviaQuestionsUseCase {
        return FetchTriviaQuestionsUseCase(getVolleyInstance(), getTriviaApiEndpoints(), getQuestionsSchemaParser())
    }

    fun getFetchTriviaCategoriesUseCase(): FetchTriviaCategoriesUseCase {
        return FetchTriviaCategoriesUseCase(getVolleyInstance(), getTriviaApiEndpoints(), getCategoriesSchemaParser())
    }

    private fun getCategoriesSchemaParser(): ICategoriesSchemaParser {
        return CategoriesSchemaParser()
    }

    private fun getQuestionsSchemaParser(): IQuestionsSchemaParser {
        return QuestionsSchemaParser(getHtmlParser())
    }

    private fun getHtmlParser(): IHtmlParser {
        return HtmlParser()
    }

    private fun getTriviaApiEndpoints(): ITriviaApiEndpoints {
        return TriviaApiEndpoints(getTriviaApiProperties())
    }

    private fun getTriviaApiProperties(): ITriviaApiProperties {
        return TriviaApiProperties(activity)
    }

    fun getTriviaGameController(): ITriviaGameController {
        return TriviaGameController(this)
    }

    fun getDialogManager(): DialogsManager {
        return DialogsManager(activity, getFragmentManager())
    }

    fun getScreensNavigator(): ScreensNavigator {
        return ScreensNavigator(activity)
    }

    fun getMessagesDisplayer(): OverlayMessagesHelper {
        return OverlayMessagesHelper(activity)
    }

    fun getActivityUtils(): ActivityUtils {
        return ActivityUtils(activity)
    }

    fun getDialogsEventBus(): DialogsEventBus {
        return compositionRoot.getDialogsEventBus()
    }
}