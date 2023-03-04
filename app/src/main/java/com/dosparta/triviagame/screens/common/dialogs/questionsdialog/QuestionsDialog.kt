package com.dosparta.triviagame.screens.common.dialogs.questionsdialog

import android.app.Dialog
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.dosparta.triviagame.R
import com.dosparta.triviagame.screens.common.dialogs.BaseDialog
import com.dosparta.triviagame.screens.common.dialogs.DialogsEventBus

class QuestionsDialog: BaseDialog() {

    companion object {
        fun newQuestionsDialog(): QuestionsDialog{
            return QuestionsDialog()
        }
    }

    private lateinit var spinnerQuestionsAmount: Spinner
    private lateinit var btnContinue: Button

    private var dialogsEventBus: DialogsEventBus? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogsEventBus = getCompositionRoot().getDialogsEventBus()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setContentView(R.layout.dialog_questions)

        spinnerQuestionsAmount = dialog.findViewById(R.id.spinner_questions_amount)
        btnContinue = dialog.findViewById(R.id.button_continue)

        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.questions_amount,
            //android.R.layout.simple_spinner_item
            R.layout.spinner_questions_amount_row
        ).also { adapter ->
            //adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            adapter.setDropDownViewResource(R.layout.spinner_questions_amount_row)
            spinnerQuestionsAmount.adapter = adapter
        }

        btnContinue.setOnClickListener { onContinueButtonClicked() }

        return dialog
    }

    private fun onContinueButtonClicked() {
        dismiss()
        val questionsAmount = (spinnerQuestionsAmount.selectedItem as String)
        dialogsEventBus?.postEvent(QuestionsDialogEvent(questionsAmount))
    }
}