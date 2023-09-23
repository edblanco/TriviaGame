package com.dosparta.triviagame.screens.common.dialogs.questionsdialog

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import com.dosparta.triviagame.R
import com.dosparta.triviagame.categories.NO_CATEGORY_ID
import com.dosparta.triviagame.categories.TriviaCategoryIn
import com.dosparta.triviagame.screens.common.dialogs.BaseDialog
import com.dosparta.triviagame.screens.common.dialogs.DialogsEventBus

class QuestionsDialog(private var categories: List<TriviaCategoryIn>): BaseDialog() {

    companion object {
        fun newQuestionsDialog(categories: List<TriviaCategoryIn>): QuestionsDialog{
            return QuestionsDialog(categories)
        }
    }

    private lateinit var spinnerQuestionsAmount: Spinner
    private lateinit var spinnerQuestionsCategories: Spinner
    private lateinit var btnContinue: Button

    private var dialogsEventBus: DialogsEventBus? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogsEventBus = getCompositionRoot().getDialogsEventBus()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val dialog = super.onCreateDialog(savedInstanceState)
        dialog.setContentView(R.layout.dialog_questions)

        fillData(dialog)

        return dialog
    }

    private fun fillData(dialog: Dialog) {
        initMembers(dialog)
        fillSpinners()
        btnContinue.setOnClickListener { onContinueButtonClicked() }
        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }

    private fun fillSpinners() {
        fillQuestionsAmountSpinner()
        fillCategoriesSpinner()
    }

    private fun fillCategoriesSpinner() {
        val spinnerArrayAdapter = ArrayAdapter(
            this.requireContext(),
            R.layout.spinner_category_row_item,
            getStringCategories()
        )

        spinnerArrayAdapter.setDropDownViewResource(R.layout.spinner_category_row_item)
        spinnerQuestionsCategories.adapter = spinnerArrayAdapter
    }

    private fun getStringCategories(): List<String> {
        val categoriesString = mutableListOf<String>()
        categories.forEach {
            categoriesString.add(it.name)
        }
        return categoriesString
    }

    private fun fillQuestionsAmountSpinner() {
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
    }

    private fun initMembers(dialog: Dialog) {
        spinnerQuestionsAmount = dialog.findViewById(R.id.spinner_questions_amount)
        spinnerQuestionsCategories = dialog.findViewById(R.id.spinner_questions_categories)
        btnContinue = dialog.findViewById(R.id.button_continue)
    }

    private fun onContinueButtonClicked() {
        dismiss()
        val questionsAmount = (spinnerQuestionsAmount.selectedItem as String)
        val category = getSelectedCategoryId()
        dialogsEventBus?.postEvent(QuestionsDialogEvent(questionsAmount, category))
    }

    private fun getSelectedCategoryId(): TriviaCategoryIn {
        kotlin.runCatching {
            return categories[spinnerQuestionsCategories.selectedItemPosition]
        }
        return TriviaCategoryIn(NO_CATEGORY_ID, "")
    }

    fun updateCategories(categories: List<TriviaCategoryIn>) {
        this.categories = categories
        fillCategoriesSpinner()
    }
}