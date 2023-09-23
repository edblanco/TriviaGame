package com.dosparta.triviagame.categories

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

const val NO_CATEGORY_ID = -1
@Parcelize
data class TriviaCategoryIn(val id: Int, val name: String) : Parcelable
