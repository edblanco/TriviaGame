package com.dosparta.triviagame.screens.common.dialogs

import androidx.fragment.app.DialogFragment
import com.dosparta.triviagame.common.TriviaGameApplication
import com.dosparta.triviagame.common.dependencyinjection.ControllerCompositionRoot

open class BaseDialog: DialogFragment() {

    private var controllerCompositionRoot: ControllerCompositionRoot? = null

    protected fun getCompositionRoot(): ControllerCompositionRoot {
        controllerCompositionRoot ?: ControllerCompositionRoot((requireActivity().application as TriviaGameApplication).getCompositionRoot(), requireActivity()).also {
            controllerCompositionRoot = it
        }
        return controllerCompositionRoot!!
    }
}