package com.nads.epicureapp.ui.homepage.profiledir.models

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class ResultWFoodId(@Expose
                     @SerializedName("status")
                         val status: Boolean? = false,
                         @Expose
                     @SerializedName("foodid")
                         val foodid: String?)