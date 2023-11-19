package com.dosparta.triviagame.screens.common.dialogs

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
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

    protected fun setTransparentBackground(dialog: Dialog) {
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }
}