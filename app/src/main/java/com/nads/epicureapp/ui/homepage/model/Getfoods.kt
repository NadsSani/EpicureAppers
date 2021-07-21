package com.nads.epicureapp.ui.homepage.model

data class Getfoods(
    val datavalues: List<DatavalueX>,
    val nextpage: Int,
    val perpage: Int,
    val previouspage: Int,
    val status: Boolean,
    val total: Int,
    val totalpages: Int,
    val likes:Int,
    val rating:Float
)