package com.dosparta.triviagame.screens.trivia.answersitem

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach

import org.junit.jupiter.api.Test
import org.mockito.ArgumentCaptor
import org.mockito.Mockito
import org.mockito.kotlin.any
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import org.xmlpull.v1.XmlPullParser

// Testing this class requires many changes within the project
internal class AnswersItemViewMvcTest {

    private lateinit var classUnderTest: AnswersItemViewMvc

    private lateinit var layoutInflaterMock: LayoutInflater
    private lateinit var layoutInflaterTd: LayoutInflater
    private lateinit var parentMock: ViewGroup
    private lateinit var UIConfigMock: IUIAnswersItemConfig
    private lateinit var viewMock: View
    private lateinit var buttonViewMock: Button
    private lateinit var contextMock: Context

    @BeforeEach
    fun setup() {
        viewMock = Mockito.mock(View::class.java)
        buttonViewMock = Mockito.mock(Button::class.java)
        contextMock = Mockito.mock(Context::class.java)
        layoutInflaterMock = Mockito.mock(LayoutInflater::class.java)
        //layoutInflaterTd = LayoutInflaterTd(contextMock, viewMock)
        parentMock = Mockito.mock(ViewGroup::class.java)
        UIConfigMock = Mockito.mock(IUIAnswersItemConfig::class.java)
        classUnderTest = AnswersItemViewMvc(layoutInflaterTd, parentMock, UIConfigMock)
        //whenever(layoutInflaterMock.inflate(Mockito.anyInt(), Mockito.eq(parentMock), Mockito.eq(false))).thenReturn(viewMock)
        whenever(viewMock.findViewById<View>(Mockito.anyInt())).thenReturn(buttonViewMock)
    }
    /*
    @Test
    fun updateTintColor_readColorFromConfig_successColor() {
        //Arrange
        val captor = ArgumentCaptor.forClass(Boolean::class.java)
        //Act
        classUnderTest.updateTintColor(true)
        //Assert
        verify(UIConfigMock).getAnswerTintColor(captor.capture())
        assertTrue(captor.value)
    }


    private class LayoutInflaterTd(context: Context?, private val view: View) : LayoutInflater(context) {
        override fun cloneInContext(p0: Context?): LayoutInflater {
            TODO("Not yet implemented")
        }

        override fun inflate(
            resource: Int,
            root: ViewGroup?,
            attachToRoot: Boolean
        ): View {
            return view
        }

    }*/

}