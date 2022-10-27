package com.dosparta.triviagame.screens.common

import android.content.Context
import android.view.View

abstract class BaseViewMvc: ViewMvc {
    private var rootView: View? = null

    override fun getRootView(): View {
        return rootView!!;
    }

    protected fun setRootView(rootView: View) {
        this.rootView = rootView
    }

    protected fun <T: View> findViewById(id: Int) : T {
        return getRootView().findViewById(id)
    }

    protected fun getContext(): Context {
        return getRootView().context
    }
}