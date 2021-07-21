package com.nads.epicureapp.ui.homepage.model

data class EpicFoodDetails(
    val datavalues: List<DatavalueXXXX>,
    val datavaluesforcooks: List<Datavaluesforcook>,
    val dataforlikerating: List<Dataforlikerating>?,
    val status: Boolean,
    val likecount:Int,
    val avrating:Float
)