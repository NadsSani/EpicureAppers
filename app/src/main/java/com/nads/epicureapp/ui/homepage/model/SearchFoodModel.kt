package com.nads.epicureapp.ui.homepage.model

data class SearchFoodModel(
    val datavalues: List<DatavalueXX>,
    val nextpage: Int?,
    val perpage: Int,
    val previouspage:Int?,
    val status: Boolean,
    val total: Int,
    val totalpages: Int
)