package com.dosparta.triviagame.screens.common.dialogs.promptdialog

import android.app.Dialog
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import com.dosparta.triviagame.R
import com.dosparta.triviagame.screens.common.dialogs.BaseDialog
import com.dosparta.triviagame.screens.common.dialogs.DialogsEventBus

// todo remove open if not inherited
open class PromptDialog : BaseDialog() {

    companion object {
        protected const val ARG_TITLE = "ARG_TITLE"
        protected const val ARG_MESSAGE = "ARG_MESSAGE"
        protected const val ARG_POSITIVE_BUTTON_CAPTION = "ARG_POSITIVE_BUTTON_CAPTION"
        protected const val ARG_NEGATIVE_BUTTON_CAPTION = "ARG_NEGATIVE_BUTTON_CAPTION"
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

    // todo remove m from members
    private lateinit var mTxtTitle: TextView
    private lateinit var mTxtMessage: TextView
    private lateinit var mBtnPositive: Button
    private lateinit var mBtnNegative: Button

    private var dialogsEventBus: DialogsEventBus? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        dialogsEventBus = getCompositionRoot().getDialogsEventBus()
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        checkNotNull(arguments) { "arguments mustn't be null" }
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_prompt)
        mTxtTitle = dialog.findViewById(R.id.txt_title)
        mTxtMessage = dialog.findViewById(R.id.txt_message)
        mBtnPositive = dialog.findViewById(R.id.btn_positive)
        mBtnNegative = dialog.findViewById(R.id.btn_negative)
        arguments?.let {
            mTxtTitle.text = arguments?.getString(ARG_TITLE)
            mTxtMessage.text = arguments?.getString(ARG_MESSAGE)
            mBtnPositive.text = arguments?.getString(ARG_POSITIVE_BUTTON_CAPTION)
            mBtnNegative.text = arguments?.getString(ARG_NEGATIVE_BUTTON_CAPTION)
        }
        mBtnPositive.setOnClickListener { onPositiveButtonClicked() }
        mBtnNegative.setOnClickListener { onNegativeButtonClicked() }
        return dialog
    }

    protected fun onPositiveButtonClicked() {
        dismiss()
        dialogsEventBus?.postEvent(PromptDialogEvent(PromptDialogEvent.Button.POSITIVE))
    }

    protected fun onNegativeButtonClicked() {
        dismiss()
        dialogsEventBus?.postEvent(PromptDialogEvent(PromptDialogEvent.Button.NEGATIVE))
    }
}