package com.nads.epicureapp.ui.homepage.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import com.nads.epicureapp.model.apiservice.datamodels.Datavalues

data class CardForfoodList(@Expose
                           @SerializedName("imgurl")
                           val imgurl:String,
                           @Expose
                           @SerializedName("foodname")
                           val foodname:String)