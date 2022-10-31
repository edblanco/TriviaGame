package com.dosparta.triviagame.screens.trivia

import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.dosparta.triviagame.TriviaGameApplication
import com.dosparta.triviagame.questions.FetchTriviaQuestionsUseCase
import com.dosparta.triviagame.questions.Question
import com.dosparta.triviagame.screens.common.BaseActivity

class TriviaGameActivity : BaseActivity() {

    private var _triviaGameController: TriviaGameController? = null
    private val triviaGameController get() = _triviaGameController!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val viewMvc: ITriviaGameViewMvc = getCompositionRoot().getViewMvcFactory().getTriviaGameViewMvc(null)
        _triviaGameController = getCompositionRoot().getTriviaGameController()

        triviaGameController.bindView(viewMvc)

        setContentView(viewMvc.getRootView())
    }

    override fun onStart() {
        super.onStart()
        triviaGameController.onStart()
    }

    override fun onStop() {
        super.onStop()
        triviaGameController.onStop()
    }

    companion object {
        fun start(context: Context) {
            val intent = Intent(context, TriviaGameActivity::class.java)
            context.startActivity(intent)
        }
    }
}