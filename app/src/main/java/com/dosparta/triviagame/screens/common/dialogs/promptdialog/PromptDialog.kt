package com.dosparta.triviagame.screens.common.dialogs.promptdialog

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.dosparta.triviagame.R
import com.dosparta.triviagame.screens.common.dialogs.BaseDialog
import com.dosparta.triviagame.screens.common.dialogs.DialogsEventBus

class PromptDialog : BaseDialog() {

    companion object {
        private const val ARG_TITLE = "ARG_TITLE"
        private const val ARG_MESSAGE = "ARG_MESSAGE"
        private const val ARG_POSITIVE_BUTTON_CAPTION = "ARG_POSITIVE_BUTTON_CAPTION"
        private const val ARG_NEGATIVE_BUTTON_CAPTION = "ARG_NEGATIVE_BUTTON_CAPTION"
        fun newPromptDialog(
            title: String?,
            message: String?,
            positiveButtonCaption: String?,
            negativeButtonCaption: String?
        ): PromptDialog {
            val promptDialog = PromptDialog()
            val args = Bundle(4)
            args.putString(ARG_TITLE, title)
            args.putString(ARG_MESSAGE, message)
            args.putString(ARG_POSITIVE_BUTTON_CAPTION, positiveButtonCaption)
            args.putString(ARG_NEGATIVE_BUTTON_CAPTION, negativeButtonCaption)
            promptDialog.arguments = args
            return promptDialog
        }
    }

    private lateinit var txtTitle: TextView
    private lateinit var txtMessage: TextView
    private lateinit var btnPositive: Button
    private lateinit var btnNegative: Button

    private var dialogsEventBus: DialogsEventBus? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogsEventBus = getCompositionRoot().getDialogsEventBus()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        checkNotNull(arguments) { "arguments mustn't be null" }
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_prompt)
        txtTitle = dialog.findViewById(R.id.txt_title)
        txtMessage = dialog.findViewById(R.id.txt_message)
        btnPositive = dialog.findViewById(R.id.btn_positive)
        btnNegative = dialog.findViewById(R.id.btn_negative)
        arguments?.let {
            txtTitle.text = arguments?.getString(ARG_TITLE)
            txtMessage.text = arguments?.getString(ARG_MESSAGE)
            btnPositive.text = arguments?.getString(ARG_POSITIVE_BUTTON_CAPTION)
            btnNegative.text = arguments?.getString(ARG_NEGATIVE_BUTTON_CAPTION)
        }
        btnPositive.setOnClickListener { onPositiveButtonClicked() }
        btnNegative.setOnClickListener { onNegativeButtonClicked() }
        setTransparentBackground(dialog)
        return dialog
    }

    private fun onPositiveButtonClicked() {
        dismiss()
        dialogsEventBus?.postEvent(PromptDialogEvent(PromptDialogEvent.Button.POSITIVE))
    }

    private fun onNegativeButtonClicked() {
        dismiss()
        dialogsEventBus?.postEvent(PromptDialogEvent(PromptDialogEvent.Button.NEGATIVE))
    }
}