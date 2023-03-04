package com.dosparta.triviagame.screens.common.dialogs.infodialog

import android.app.Dialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.annotation.NonNull
import com.dosparta.triviagame.R
import com.dosparta.triviagame.screens.common.dialogs.BaseDialog


open class InfoDialog : BaseDialog() {

    companion object {
        protected const val ARG_TITLE = "ARG_TITLE"
        protected const val ARG_MESSAGE = "ARG_MESSAGE"
        protected const val ARG_BUTTON_CAPTION = "ARG_BUTTON_CAPTION"
        fun newInfoDialog(title: String?, message: String?, buttonCaption: String?): InfoDialog {
            val infoDialog = InfoDialog()
            val args = Bundle(3)
            args.putString(ARG_TITLE, title)
            args.putString(ARG_MESSAGE, message)
            args.putString(ARG_BUTTON_CAPTION, buttonCaption)
            infoDialog.arguments = args
            return infoDialog
        }
    }

    private lateinit var mTxtTitle: TextView
    private lateinit var mTxtMessage: TextView
    private lateinit var mBtnPositive: Button

    @NonNull
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        checkNotNull(arguments) { "arguments mustn't be null" }
        val dialog = Dialog(requireContext())
        dialog.setContentView(R.layout.dialog_info)
        mTxtTitle = dialog.findViewById(R.id.txt_title)
        mTxtMessage = dialog.findViewById(R.id.txt_message)
        mBtnPositive = dialog.findViewById(R.id.btn_positive)
        mTxtTitle.text = requireArguments().getString(ARG_TITLE)
        mTxtMessage.text = requireArguments().getString(ARG_MESSAGE)
        mBtnPositive.text = requireArguments().getString(ARG_BUTTON_CAPTION)
        mBtnPositive.setOnClickListener { onButtonClicked() }
        return dialog
    }

    protected fun onButtonClicked() {
        dismiss()
    }
}
