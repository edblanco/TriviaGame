package com.dosparta.triviagame.screens.trivia

import android.os.Bundle

interface ITriviaGameController {
    fun bindView(viewMvc: ITriviaGameViewMvc)
    fun onStart()
    fun onSaveInstanceState(outState: Bundle)
    fun onResume()
    fun onRestoreInstanceState(savedInstanceState: Bundle)
    fun onStop()

}